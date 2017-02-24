package xyz.gnarbot.gnar.commands.executors.admin

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.servers.Shard
import xyz.gnarbot.gnar.utils.Note

@Command(
        aliases = arrayOf("gc"),
        description = "Request Java to garbage collect.",
        level = Level.BOT_CREATOR,
        showInHelp = false
)
class GarbageCollectCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        note.embed("Garbage Collection") {
            Bot.shards.forEach(Shard::clearServlets)
            field("Wrappers", false, "Removed references to wrappers.")

            field("Guild Servlets Remaining", true, Bot.shards.sumBy { it.servlets.size })
            field("Member Clients Remaining", true, Bot.shards.flatMap { it.servlets.values }.sumBy { it.clientHandler.registry.size })

            System.gc()
            field("GC Request", false, "Garbage collection request sent to JVM.")
            Bot.LOGGER.info("Garbage collection request sent to JVM.")
        }.rest().queue()
    }
}