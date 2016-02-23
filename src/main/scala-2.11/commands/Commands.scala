package commands

import org.bukkit.command.{Command, CommandSender, CommandExecutor}

/**
 * Created by Kyle on 9/30/2015.
 */
class Commands extends CommandExecutor{
  override def onCommand(sender: CommandSender, cmd: Command, label: String, args: Array[String]): Boolean = false
}
