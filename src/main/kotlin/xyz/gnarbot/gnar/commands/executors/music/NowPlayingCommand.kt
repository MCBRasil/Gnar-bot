package xyz.gnarbot.gnar.commands.executors.music

import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command

@Command(aliases = arrayOf("nowplaying", "np"),
        description = "Shows what's currently playing.",
        category = Category.MUSIC)
class NowPlayingCommand : MusicExecutor() {

    override fun execute(message: Message, args: List<String>) {
        val track = guildData.musicManager.player.playingTrack

        if (track == null) {
            message.respond().error("The player is not currently playing anything.")
            return
        }

        message.respond().embed("Now Playing") {
            field("Now Playing", false, "__[${track.info.title}](${track.info.uri})__")

            val position = getTimestamp(track.position)
            val duration = getTimestamp(track.duration)

            field("Time", true, "**[$position / $duration]**")
        }.rest().queue()
    }
}
