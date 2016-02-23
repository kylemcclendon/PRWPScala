import java.util.UUID

import cassandra.CassandraConnection
import com.datastax.driver.core.{ResultSetFuture, Row}
import com.datastax.driver.core.querybuilder.QueryBuilder
import models.{WorldDTO, RankDTOMin, RankDTO, PlayerDTO}
import scala.collection.JavaConversions._
import scala.collection.immutable.HashSet

class PermissionHandler(cassandraConnection: CassandraConnection) {

  def getPlayerPermissions(playerId: UUID, worldId: UUID): Map[String, Set[String]] = {
    var allowedPermissions: Set[Int] = Set()
    var restrictions: Set[Int] = Set()

    //Get Player Value From 'player' Table
    val playerStatement = QueryBuilder.select().from(getKeyspace, "player").where(QueryBuilder.eq("playerid", playerId))
    val playerResult = Option(getSession.execute(playerStatement).one())
    val playerRow = playerResult.getOrElse(createPlayer(playerId))
    val playerDTO = new PlayerDTO(playerRow.getUUID("playerid"), playerRow.getSet("permit", classOf[Int]).toSet, playerRow.getInt("rankid"), playerRow.getSet("restrict", classOf[Int]).toSet)

    //Get Player's Rank Value From 'rank' Table
    val rankId = playerDTO.rankId
    val rankStatement = QueryBuilder.select().from(getKeyspace, "rank").where(QueryBuilder.eq("rankid", playerDTO.rankId))
    val rankResult = Option(getSession.execute(rankStatement).one())
    var rankDTO: RankDTO = null
    if(rankResult.isDefined){
      val rankRow = rankResult.get
      rankDTO = new RankDTO(rankRow.getInt("rankid"), rankRow.getString("name"), rankRow.getBool("moderator"), rankRow.getSet("permit", classOf[Int]).toSet, rankRow.getSet("restrict", classOf[Int]).toSet)

      //Get Player's World Value From 'world' Table
      val worldStatement = QueryBuilder.select().from(getKeyspace, "world").where(QueryBuilder.eq("worldid", worldId)).and(QueryBuilder.eq("rankid", rankDTO.rankId))
      val worldResult = Option(getSession.execute(worldStatement).one())
      val worldRow = worldResult.getOrElse(createWorldRank(worldId, rankDTO.rankId))
      val worldDTO = new WorldDTO(worldRow.getUUID("worldid"),worldRow.getInt("rankid"),worldRow.getSet("permit", classOf[Int]).toSet,worldRow.getSet("restrict", classOf[Int]).toSet)

      allowedPermissions = allowedPermissions ++ rankDTO.permit ++ worldDTO.permit
      restrictions = restrictions ++ rankDTO.restrict ++ rankDTO.restrict
    }
    else{
      println(s"Could not get rank permissions! RankId $rankId does not exist?")
    }

    allowedPermissions = allowedPermissions ++ playerDTO.permit
    restrictions = restrictions ++ playerDTO.restrict
    collectPermissionStringValues(allowedPermissions, restrictions)
  }

  def getSession = cassandraConnection.getSession()

  def getKeyspace = cassandraConnection.getKeySpace

  def createPlayer(playerId: UUID): Row = {
    val ranksStatement = getSession.execute(QueryBuilder.select().from(getKeyspace, "rank")).all()
    val ranks = ranksStatement.map(rank => new RankDTOMin(rank.getInt("rankid"),rank.getString("name"),rank.getBool("moderator"))).filter(rank => !rank.isModerator)
    val rank = ranks.minBy(_.rankId)
    getSession.execute(QueryBuilder.insertInto(getKeyspace, "player").value("playerid", playerId).value("rankid", rank.rankId).value("permit", new HashSet[Int]).value("restrict", new HashSet[Int]))
    val playerStatement = QueryBuilder.select().from(getKeyspace, "player").where(QueryBuilder.eq("playerid", playerId))
    getSession.execute(playerStatement).one()
  }

  def createWorldRank(worldId: UUID, rankId: Int): Row ={
    getSession.execute(QueryBuilder.insertInto(getKeyspace, "world").value("worldid", worldId).value("rankid", rankId).value("permit", new HashSet[String]).value("restrict", new HashSet[String]))
    val worldStatement = QueryBuilder.select().from(getKeyspace, "world").where(QueryBuilder.eq("worldid", worldId)).and(QueryBuilder.eq("rankid", rankId))
    getSession.execute(worldStatement).one()
  }

  def collectPermissionStringValues(allow: Set[Int], restrict: Set[Int]): Map[String, Set[String]] = {
    Map()
  }

  /*
  PreparedStatement statement = session.prepare(
  "SELECT * FROM tester.users where id = ?");
List<ResultSetFuture> futures = new ArrayList<>();
for (int i = 1; i < 4; i++) {
 ResultSetFuture resultSetFuture = session.executeAsync(statement.bind(i));
 futures.add(resultSetFuture);
}
List<String> results = new ArrayList<>();
for (ResultSetFuture future : futures){
 ResultSet rows = future.getUninterruptibly();
 Row row = rows.one();
 results.add(row.getString("name"));
}
return results;

   */

}

object PermissionHandler extends PermissionHandler(CassandraConnection)