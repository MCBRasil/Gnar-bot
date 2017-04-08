package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(aliases = arrayOf("repeat"),
        description = "Set if the music player should repeat.",
        category = Category.MUSIC,
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class RepeatCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        val manager = guildData.musicManager

        manager.scheduler.isRepeating = !manager.scheduler.isRepeating

        message.respond().embed("Repeat Queue") {
            color = Constants.MUSIC_COLOR
            description = "Music player was set to __${if (manager.scheduler.isRepeating) "repeat" else "not repeat"}__."
        }.rest().queue()
    }
}
