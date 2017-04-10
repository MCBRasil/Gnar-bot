package xyz.gnarbot.gnar.commands.executors.music

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.Utils

@Command(
        aliases = arrayOf("nowplaying", "np"),
        description = "Shows what's currently playing.",
        category = Category.MUSIC
)
class NowPlayingCommand : CommandExecutor() {

    override fun execute(message: Message, args: Array<String>) {
        val track = guildData.musicManager.player.playingTrack

        if (track == null) {
            message.respond().error("The player is not currently playing anything.")
            return
        }

        message.respond().embed("Now Playing") {
            color = Constants.MUSIC_COLOR

            field("Now Playing", false, "__[${track.info.title}](${track.info.uri})__")

            val position = Utils.getTimestamp(track.position)
            val duration = Utils.getTimestamp(track.duration)

            field("Time", true, "**[$position / $duration]**")
        }.rest().queue()
    }
}
