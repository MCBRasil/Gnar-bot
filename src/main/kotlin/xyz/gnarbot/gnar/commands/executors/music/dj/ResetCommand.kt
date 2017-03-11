package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.servers.Servlet
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("reset"), level = Level.DJ, description = "Completely reset the music player.")
class ResetCommand : MusicExecutor() {

    @Inject lateinit var servlet: Servlet
    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        servlet.resetMusicManager()

        note.respond().embed("Reset Music") {
            color = musicColor
            description = "The player was completely reset."
        }.rest().queue()
    }
}
