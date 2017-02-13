package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
import net.dv8tion.jda.core.EmbedBuilder
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("nowplaying", "np"))
class NowPlayingCommand : MusicExecutor() {

    @Inject lateinit private var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        val track = manager.player.playingTrack

        if (track == null) {
            note.error("The player is not currently playing anything.")
            return
        }

        val eb = EmbedBuilder()

        eb.setTitle("Now Playing", null)
        if (track.sourceManager.sourceName.contains("youtube")) {
            eb.setDescription("__[${track.info.title}](https://youtube.com/watch?v=${track.info.identifier})__")
        } else {
            eb.setDescription("__[${track.info.title}]()__")
        }

        val position = getTimestamp(track.position)
        val duration = getTimestamp(track.duration)

        eb.addField("Time", "**[$position / $duration]**", true)

        eb.setColor(color)

        note.channel.sendMessage(eb.build()).queue()
    }
}
