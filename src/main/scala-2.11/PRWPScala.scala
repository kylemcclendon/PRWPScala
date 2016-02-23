import cassandra.{CassandraConnectionImpl, CassandraConnection}
import com.datastax.driver.core.Session
import com.sk89q.wepif.PermissionsProvider
import commands.Commands
import files.PermFileHandler
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import utils.Utils

class PRWPScala extends JavaPlugin with PermissionsProvider{
  val dFolder = getDataFolder
  var pc: Commands = _
  var cassandraConnection: CassandraConnection = _

  override def onEnable(): Unit = {
    Utils.loadWorlds()
    cassandraConnection = new CassandraConnectionImpl

    val pfh = new PermFileHandler(dFolder)
  }

  override def onDisable(): Unit = {

  }

  override def hasPermission(name: String, permission: String): Boolean = false

  override def hasPermission(worldName: String, name: String, permission: String): Boolean = false

  override def hasPermission(player: OfflinePlayer, permission: String): Boolean = false

  override def hasPermission(worldName: String, player: OfflinePlayer, permission: String): Boolean = false

  override def inGroup(player: String, group: String): Boolean = false

  override def inGroup(player: OfflinePlayer, group: String): Boolean = false

  override def getGroups(player: String): Array[String] = null

  override def getGroups(player: OfflinePlayer): Array[String] = null
}