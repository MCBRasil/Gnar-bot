package xyz.gnarbot.gnar.commands.executors.admin

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(
        aliases = arrayOf("gc"),
        description = "Request Java to garbage collect.",
        administrator = true,
        category = Category.NONE
)
class GarbageCollectCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        message.respond().embed("Garbage Collection") {
            color = Constants.COLOR
            val interrupt = if (!args.isEmpty()) args[0].toBoolean() else false

            bot.shards.forEach { it.clearServlets(interrupt) }
            field("Wrappers", false, "Removed references to wrappers.")

            field("Guild Servlets Remaining", true, bot.shards.sumBy { it.servlets.size })

            System.gc()
            field("GC Request", false, "Garbage collection request sent to JVM.")
            bot.log.info("Garbage collection request sent to JVM.")
        }.rest().queue()
    }
}