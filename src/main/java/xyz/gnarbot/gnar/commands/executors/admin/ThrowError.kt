package xyz.gnarbot.gnar.commands.executors.admin

import xyz.gnarbot.gnar.commands.handlers.*
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.utils.Note

@Command(
        aliases = arrayOf("throwError"),
        clearance = Clearance.BOT_MASTER,
        showInHelp = false
)
class ThrowError : CommandExecutor()
{
    override fun execute(message : Note, label : String, args : Array<out String>)
    {
        throw RuntimeException("Requested to throw an error, so here you go.")
    }
}