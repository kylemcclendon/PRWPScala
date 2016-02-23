package files

import java.io._
import java.util
import org.bukkit.configuration.file.YamlConfiguration
import utils.Utils

class PermFileHandler(dFolder: File) {
  private var namesSettings: YamlConfiguration = null
  private var ranksSettings: YamlConfiguration = null
  private var playerRanksFile: File = null
  private var playerRanksSettings: YamlConfiguration = null

  initConfigFile()
  initNamesFile()
  initPlayerRanksFile()
  createBaseFolders()
  createGroups(new File(dFolder, "Groups"))
  createWorldFolders()
  createWorldGroups()

  def initConfigFile() = {
    val configFile = new File(dFolder, "config.yml")
    if (!configFile.exists) {
      val configFileSettings = new YamlConfiguration
      configFileSettings.set("groups", "#EXAMPLE: guest,member,veteran")
      configFileSettings.set("mods", "#EXAMPLE: op,admin")
      configFileSettings.set("block-blacklist", "#EXAMPLE: 1,2,3,4")
      configFileSettings.set("block-whitelist", "#EXAMPLE: 1,2,3,4")
      saveConfiguration(configFileSettings, configFile)
    }

    val configSettings = YamlConfiguration.loadConfiguration(configFile)
    checkSettings(configSettings)
    initRanks(configSettings)
  }

  def initNamesFile() = {
    val namesFile = new File(dFolder, "names.yml")
    if(!namesFile.exists){
      val namesConfiguration = new YamlConfiguration
      namesConfiguration.set("#", "Example")
      saveConfiguration(namesConfiguration, namesFile)
    }
    namesSettings = YamlConfiguration.loadConfiguration(namesFile)
  }

  def initPlayerRanksFile() = {
    playerRanksFile = new File(dFolder, "playerRanks.yml")
    if(!playerRanksFile.exists){
      val playerRanksConfiguration = new YamlConfiguration
      saveConfiguration(playerRanksConfiguration, playerRanksFile)
    }
    playerRanksSettings = YamlConfiguration.loadConfiguration(playerRanksFile)
  }

  def saveConfiguration(configuration: YamlConfiguration, file: File) {
    if(!file.exists) file.createNewFile()
    try {
      configuration.save(file)
    }
    catch {
      case ioe: IOException => ioe.printStackTrace()
    }
  }

  //TODO:Finish method
  def checkSettings(config: YamlConfiguration) = {
    if(config.getString("groups") == null || config.getString("groups").contains("#")){
      Utils.setGroupNames(Set{""})
    }
    else{
      Utils.setGroupNames(config.getString("groups").split(",").toSet)
    }

    if(config.getString("mods") == null || config.getString("mods").contains("#")) {
      Utils.setModNames(Set{""})
    }
    else{
      Utils.setModNames(config.getString("mods").split(",").toSet)
    }

    if(config.getString("block-blacklist") == null || config.getString("block-blacklist").contains("#")){
      Utils.setBlockBlacklist(Set{""})
    }
    else{
      Utils.setBlockBlacklist(config.getString("block-blacklist").split(",").toSet)
    }

    if(config.getString("block-whitelist") == null || config.getString("block-whitelist").contains("#")){
      Utils.setBlockWhitelist(Set{""})
    }
    else{
      Utils.setBlockWhitelist(config.getString("block-whitelist").split(",").toSet)
    }


  }

  def initRanks(config: YamlConfiguration): Unit = {
    val ranksFile = new File(dFolder, "ranks.yml")
    if(!ranksFile.exists){
      val ranksConfiguration = new YamlConfiguration
      saveConfiguration(ranksConfiguration, ranksFile)
    }
    ranksSettings = YamlConfiguration.loadConfiguration(ranksFile)

    val existingRanks: util.Set[String] = ranksSettings.getKeys(false)
    val ranks = config.getString("groups").split(",") ++ config.getString("mods").split("\\s*,\\s*")

    for(rank <- ranks){
      if(!existingRanks.contains(rank)){
        ranksSettings.set(rank, "[&#_&F]")
      }
    }
    saveConfiguration(ranksSettings, ranksFile)
  }

  def createBaseFolders() = {
    val worldsFolder = new File(dFolder, "Worlds")
    if(!worldsFolder.exists){
      worldsFolder.mkdir
    }

    val groupsFolder = new File(dFolder, "Groups")
    if(!groupsFolder.exists){
      groupsFolder.mkdir
    }

    val playersFolder = new File(dFolder, "Players")
    if(!playersFolder.exists){
      playersFolder.mkdir
    }
  }

  def createWorldFolders() = {
    val worldsFolder = new File(dFolder, "Worlds")
    for(world <- Utils.getWorlds){
      val newWorld = new File(worldsFolder, world)
      if(!newWorld.exists){
        newWorld.mkdir()
      }
    }
  }

  def createWorldGroups() = {
    val worldsFolder = new File(dFolder, "Worlds")
    for(world <- Utils.getWorlds){
      val worldFolder = new File(worldsFolder, world)
      createGroups(worldFolder)
    }
  }

  def createGroups(directory: File) = {
    val allGroup = new File(directory, "_all.txt")
    if(!allGroup.exists){
      writeDefaultGroup(allGroup)
    }

    for(groupName <- Utils.getGroupNames){
      if(!groupName.equals("")){
        val groupFile = new File(directory, groupName + ".txt")
        if (!groupFile.exists) {
          writeDefaultGroup(groupFile)
        }
      }
    }

    for(modName <- Utils.getModNames){
      if(!modName.equals("")){
        val modFile = new File(directory, modName + ".txt")
        if (!modFile.exists) {
          writeDefaultGroup(modFile)
        }
      }
    }
  }

  private def writeDefaultGroup(file: File) = {
    try{
      val writer = new BufferedWriter(new FileWriter(file))
      writer.write("Permissions\n--------------")
    }
    catch {
      case ioe: IOException => ioe.printStackTrace()
    }
  }
}