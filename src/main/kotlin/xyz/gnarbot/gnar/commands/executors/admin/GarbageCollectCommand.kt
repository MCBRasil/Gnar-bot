package xyz.gnarbot.gnar.commands.executors.admin

import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(
        aliases = arrayOf("gc"),
        description = "Request Java to garbage collect.",
        level = Level.BOT_CREATOR,
        showInHelp = false
)
class GarbageCollectCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        note.respond().embed("Garbage Collection") {
            color = Constants.COLOR
            val interrupt = if (!args.isEmpty()) args[0].toBoolean() else false

            bot.shards.forEach { it.clearServlets(interrupt) }
            field("Wrappers", false, "Removed references to wrappers.")

            field("Guild Servlets Remaining", true, bot.shards.sumBy { it.servlets.size })
            field("Member Clients Remaining", true, bot.shards.flatMap { it.servlets.values }.sumBy { it.clientHandler.registry.size })

            System.gc()
            field("GC Request", false, "Garbage collection request sent to JVM.")
            bot.log.info("Garbage collection request sent to JVM.")
        }.rest().queue()
    }
}