package xyz.gnarbot.gnar.commands.executors.admin

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.*
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.utils.Note

@Command(
        aliases = arrayOf("gc"),
        description = "Run JavaScript commands.",
        clearance = Clearance.BOT_MASTER,
        showInHelp = false
)
class GarbageCollectCommand : CommandExecutor()
{
    override fun execute(note : Note, label : String, args : Array<out String>)
    {
        System.gc()
        
        note.info("Garbage collection request sent to JVM.")
        Bot.LOG.info("Garbage collection request sent to JVM.")
    }
}