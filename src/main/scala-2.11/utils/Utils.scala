package utils

import org.bukkit.Bukkit
import scala.collection.JavaConverters._

object Utils {
  private var worldNames: Set[String] = _
  private var groupNames: Set[String] = _
  private var modNames: Set[String] = _
  private var blockBlacklist: Set[String] = _
  private var blockWhitelist: Set[String] = _

  def loadWorlds() = {
     worldNames = Bukkit.getServer.getWorlds.asScala.map(world => world.getName).toSet
  }

  def getWorlds = worldNames

  def setGroupNames(groupSet: Set[String]) = groupNames = groupSet
  def getGroupNames = groupNames

  def setModNames(modSet: Set[String]) = modNames = modSet
  def getModNames = modNames

  def setBlockBlacklist(blackListSet: Set[String]) = blockBlacklist = blackListSet
  def getBlockBlacklist = blockBlacklist

  def setBlockWhitelist(whiteListSet: Set[String]) = blockWhitelist = whiteListSet
  def getBlockWhitelist = blockWhitelist
}