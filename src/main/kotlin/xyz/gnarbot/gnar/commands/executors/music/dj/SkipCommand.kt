package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(aliases = arrayOf("skip"),
        description = "Skip the current music track.",
        category = Category.MUSIC,
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class SkipCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        val manager = guildData.musicManager

        if (manager.scheduler.queue.isEmpty()) {
            guildData.resetMusicManager()
        } else {
            manager.scheduler.nextTrack()
        }

        message.respond().embed("Skip Current Track") {
            color = Constants.MUSIC_COLOR
            description = "The track was skipped."
        }.rest().queue()
    }
}
