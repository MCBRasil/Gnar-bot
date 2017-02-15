package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
import net.dv8tion.jda.core.EmbedBuilder
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note
import java.util.*

@Command(aliases = arrayOf("queue", "list"),
        description = "Shows the music that's currently queued.")
class QueueCommand : MusicExecutor() {

    @Inject lateinit private var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        val queue = manager.scheduler.queue

        if (queue.isEmpty() && manager.player == null) {
            note.replyMusic("The queue is currently empty.")
            return
        }

        var trackCount = 0
        var queueLength = 0L

        val eb = EmbedBuilder()

        eb.setTitle("Music", null)

        manager.player.playingTrack?.let {
            if (it.sourceManager.sourceName.contains("youtube")) {
                eb.addField("Now Playing", "`[${getTimestamp(it.duration)}]` __[${it.info.title}](https://youtube.com/watch?v=${it.info.identifier})__", false)
            } else {
                eb.addField("Now Playing", "`[${getTimestamp(it.duration)}]` __[${it.info.title}]()__", false)
            }
        }

        val sj = StringJoiner("\n")
        for (track in queue) {
            queueLength += track.duration
            trackCount++
            if (track.sourceManager.sourceName.contains("youtube")){
                sj.add("**$trackCount** `[${getTimestamp(track.duration)}]` __[${track.info.title}](https://youtube.com/watch?v=${track.info.identifier})__")
            } else {
                sj.add("**$trackCount** `[${getTimestamp(track.duration)}]` __[${track.info.title}]()__")
            }

            if (trackCount >= 10) {
                sj.add("... and **${queue.size - trackCount}** more tracks.")
                break
            }
        }

        eb.addField("Queue", if (sj.length() != 0) sj.toString() else "*Empty*", false)

        eb.addField("Entries", trackCount.toString(), true)
        eb.addField("Queue Length", getTimestamp(queueLength), true)
        eb.setColor(color)

        note.channel.sendMessage(eb.build()).queue()
    }
}
