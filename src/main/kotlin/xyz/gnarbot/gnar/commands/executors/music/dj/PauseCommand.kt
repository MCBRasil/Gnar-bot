package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(aliases = arrayOf("pause"),
        description = "Pause or resume the music player.",
        category = Category.MUSIC,
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class PauseCommand : CommandExecutor() {
    override fun execute(message: Message, args: List<String>) {
        val manager = guildData.musicManager

        if (manager.player.playingTrack == null) {
            message.respond().error("Can not pause or resume player because there is no track loaded for playing.")
            return
        }

        manager.player.isPaused = !manager.player.isPaused

        message.respond().embed("Playback Control") {
            color = Constants.MUSIC_COLOR
            description = if (manager.player.isPaused) {
                "The player has been paused."
            } else {
                "The player has resumed playing."
            }
        }.rest().queue()
    }
}
