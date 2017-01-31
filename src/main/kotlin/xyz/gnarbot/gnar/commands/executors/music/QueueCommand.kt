package xyz.gnarbot.gnar.commands.executors.music

import net.dv8tion.jda.core.EmbedBuilder
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note
import java.util.*

@Command(aliases = arrayOf("queue", "list"), clearance = Clearance.USER)
class QueueCommand : MusicExecutor() {
    override fun execute(note: Note, args: List<String>, host: Host, manager: MusicManager) {
        val queue = manager.scheduler.queue

        if (queue.isEmpty() && manager.player == null) {
            note.replyMusic("The queue is currently empty.")
            return
        }

        var trackCount = 0
        var queueLength = 0L

        val eb = EmbedBuilder()
        val sj = StringJoiner("\n")

        eb.setTitle("Current Music Queue")
        var track = manager.player.playingTrack;
        if (track.sourceManager.sourceName.contains("youtube")){
            sj.add("**Playing Now :musical_note:** `[${getTimestamp(track.duration)}]` __[${track.info.title}](https://youtube.com/watch?v=${track.info.identifier})__")
        }else{
            sj.add("**Playing Now :musical_note:** `[${getTimestamp(track.duration)}]` __[${track.info.title}]()__")
        }

        for (track in queue) {
            queueLength += track.duration
            trackCount++
            if (track.sourceManager.sourceName.contains("youtube")){
                sj.add("**$trackCount** `[${getTimestamp(track.duration)}]` __[${track.info.title}](https://youtube.com/watch?v=${track.info.identifier})__")
            }else{
                sj.add("**$trackCount** `[${getTimestamp(track.duration)}]` __[${track.info.title}]()__")
            }

        }

        eb.addField("", sj.toString(), false)

        eb.addField("", "**Entries:** `${trackCount}`\n**Total queue length:** `[${getTimestamp(queueLength)}]`", false)
        eb.setColor(color)

        note.channel.sendMessage(eb.build()).queue()
    }
}
