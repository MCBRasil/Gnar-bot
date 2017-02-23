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
        Bot.shards.forEach(Shard::clearServlets)
        note.info("Removed references to wrappers.").queue()

        note.info("Detected ${Bot.shards.flatMap { it.servlets.values }.sumBy { it.clientHandler.registry.size }} guild|servlet wrappers remaining.").queue()
        note.info("Detected ${Bot.shards.sumBy { it.servlets.size }} member|client wrappers remaining.").queue()

        Bot.LOGGER.info("Garbage collection request sent to JVM.")

        System.gc()

        note.info("Garbage collection request sent to JVM.").queue()
        Bot.LOGGER.info("Garbage collection request sent to JVM.")
    }
}