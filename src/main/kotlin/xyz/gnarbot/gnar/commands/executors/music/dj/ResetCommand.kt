package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("reset"), level = Level.DJ, description = "Completely reset the music player.")
class ResetCommand : MusicExecutor() {

    @Inject lateinit var host: Host
    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        host.resetMusicManager()

        note.replyMusic("The player has been completely reset.")
    }
}
