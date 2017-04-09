package xyz.gnarbot.gnar.commands.executors.music

import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageEmbed
import u
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Utils

@Command(
        aliases = arrayOf("queue", "list"),
        description = "Shows the music that's currently queued.",
        category = Category.MUSIC
)
class QueueCommand : CommandExecutor() {

    override fun execute(message: Message, args: Array<String>) {
        val queue = guildData.musicManager.scheduler.queue

        var trackCount = 0
        var queueLength = 0L

        message.respond().embed("Music Queue") {
            color = Constants.MUSIC_COLOR

            guildData.musicManager.player.playingTrack?.let {
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

                    appendln("**$trackCount** `[${Utils.getTimestamp(track.duration)}]` __[${track.info.title}](${track.info.uri})__")

                    if (length >= MessageEmbed.VALUE_MAX_LENGTH - 100) {
                        append("... and **${queue.size - trackCount}** more tracks.")
                        break
                    }
                }
            }

            field("Entries", true, trackCount)
            field("Queue Duration", true, Utils.getTimestamp(queueLength))
        }.rest().queue()
    }
}
