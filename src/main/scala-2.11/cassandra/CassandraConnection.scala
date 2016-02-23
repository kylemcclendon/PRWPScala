package cassandra

import java.util.logging.Logger

import com.datastax.driver.core.{Session, Cluster, Host, Metadata}

trait CassandraConnection {
  def getSession(): Session
  def createSessionAndInitKeySpace(address: String, port: Int, keyspace: String, username: String, password: String): Session
  def createSession(address: String, port: Int): Session
  def getKeySpace = "minecraft"
  def getPort = 9042
  def getHost = "localhost"
  def getUserName = "cassandra"
  def getPassword = "cassandra"
}

class CassandraConnectionImpl extends CassandraConnection{
  private var session: Session = _

  def getSession(): Session = {
    if(session == null){
      createSessionAndInitKeySpace(getHost, getPort, getKeySpace, getUserName, getPassword)
    }
    session
  }

  def createSessionAndInitKeySpace(address: String, port: Int, keySpace: String, username: String, password: String): Session = {
    try {
      var hosts: Array[String] = Array(address)
      if(address.contains(",")) hosts = address.split(",").map(_.trim)

      val cluster = Cluster.builder().addContactPoints(hosts:_*).withPort(port).withCredentials(username, password).build()
      session = cluster.connect(keySpace)
    } catch {
      case ex: Exception => System.out.println("Problem Creating Session/Initializing Keyspace! " + ex.getMessage)
    }
    session
  }

  def createSession(address: String, port: Int): Session = {
    val cluster = Cluster.builder().addContactPoint(address).withPort(port).build()
    val session = cluster.connect()
    session
  }
}

object CassandraConnection extends CassandraConnectionImpl