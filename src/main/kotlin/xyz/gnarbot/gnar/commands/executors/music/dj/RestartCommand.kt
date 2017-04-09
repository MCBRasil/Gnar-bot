package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(
        aliases = arrayOf("restart"),
        description = "Restart the current song.",
        category = Category.MUSIC,
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL)
)
class RestartCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        val manager = guildData.musicManager

        var track = manager.player.playingTrack

        if (track == null) {
            track = manager.scheduler.lastTrack
        }

        if (track != null) {

            message.respond().embed("Restart Song") {
                color = Constants.MUSIC_COLOR
                description = "Restarting track: `${track.info.title}`."
            }.rest().queue()

            manager.player.playTrack(track.makeClone())
        } else {
            message.respond().error("No track has been previously started.").queue()
        }
    }
}
