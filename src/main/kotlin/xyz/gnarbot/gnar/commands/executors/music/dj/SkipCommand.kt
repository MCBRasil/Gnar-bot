package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.servers.Servlet
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("skip"), level = Level.DJ, description = "Skip the current music track.")
class SkipCommand : MusicExecutor() {
    @Inject lateinit var servlet: Servlet
    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        if (manager.scheduler.queue.isEmpty()) {
            servlet.resetMusicManager()
        } else {
            manager.scheduler.nextTrack()
        }

        note.respond().embed("Skip Current Track") {
            color = musicColor
            description = "The track was skipped."
        }.rest().queue()
    }
}
