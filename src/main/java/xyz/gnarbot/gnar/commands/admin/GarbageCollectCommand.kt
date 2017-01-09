package xyz.gnarbot.gnar.commands.admin

import xyz.gnarbot.gnar.handlers.commands.*
import xyz.gnarbot.gnar.handlers.members.Clearance
import xyz.gnarbot.gnar.utils.Note

@Command(
        aliases = arrayOf("gc"),
        description = "Run JavaScript commands.",
        clearance = Clearance.BOT_MASTER,
        showInHelp = false
)
class GarbageCollectCommand : CommandExecutor()
{
    override fun execute(message : Note, label : String, args : Array<out String>?)
    {
        System.gc()
    }
}