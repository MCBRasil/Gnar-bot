package xyz.gnarbot.gnar.commands.admin

import xyz.gnarbot.gnar.handlers.commands.*
import xyz.gnarbot.gnar.handlers.members.Clearance
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