package xyz.gnarbot.gnar.commands.executors.music

import net.dv8tion.jda.core.entities.Message
import u
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command

@Command(aliases = arrayOf("queue", "list"),
        description = "Shows the music that's currently queued.",
        category = Category.MUSIC)
class QueueCommand : MusicExecutor() {

    override fun execute(message: Message, args: List<String>) {
        val queue = servlet.musicManager.scheduler.queue

        if (queue.isEmpty()) {
            message.respond().embed("Queue") {
                color = musicColor
                description = "The queue is currently empty."
            }.rest().queue()
            return
        }

        var trackCount = 0
        var queueLength = 0L

        message.respond().embed("Music Queue") {
            color = musicColor

            servlet.musicManager.player.playingTrack?.let {
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

                    appendln("**$trackCount** `[${getTimestamp(track.duration)}]` __[${track.info.title}](${track.info.uri})__")

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
