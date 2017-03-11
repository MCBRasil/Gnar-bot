package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("queue", "list"),
        description = "Shows the music that's currently queued.")
class QueueCommand : MusicExecutor() {

    @Inject lateinit private var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        val queue = manager.scheduler.queue

        if (queue.isEmpty()) {
            note.respond().embed("Queue") {
                color = musicColor
                description = "The queue is currently empty."
            }.rest().queue()
            return
        }

        var trackCount = 0
        var queueLength = 0L

        note.respond().embed("Music Queue") {
            color = musicColor

            manager.player.playingTrack?.let {
                field("Now Playing", false, if (it.sourceManager.sourceName.contains("youtube")) {
                    "__[${it.info.title}](https://youtube.com/watch?v=${it.info.identifier})__"
                } else {
                    "__[${it.info.title}]()__"
                })
            }

            field("Queue", false) {
                if (queue.isEmpty()) {
                    append(u("Empty queue.")).append("Add some music with `_play url|YT search`.")
                } else for (track in queue) {
                    queueLength += track.duration
                    trackCount++

                    val str = if (track.sourceManager.sourceName.contains("youtube")) {
                        "**$trackCount** `[${getTimestamp(track.duration)}]` __[${track.info.title}](https://youtube.com/watch?v=${track.info.identifier})__"
                    } else {
                        "**$trackCount** `[${getTimestamp(track.duration)}]` __[${track.info.title}]()__"
                    }

                    appendln(str)

                    if (length >= 900) {
                        append("... and **${queue.size - trackCount}** more tracks.")
                        break
                    }
                }
            }

            field("Entries", true, trackCount)
            field("Queue Duration", true, getTimestamp(queueLength))
        }.rest().queue()
    }
}
