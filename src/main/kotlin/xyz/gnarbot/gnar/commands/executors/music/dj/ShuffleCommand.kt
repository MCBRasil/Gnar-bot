package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("shuffle"), level = Level.DJ, description = "Shuffle the music queue.")
class ShuffleCommand : MusicExecutor() {

    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        manager.scheduler.shuffle()

        note.embed("Shuffle Queue") {
            color(musicColor)
            description("Player has been shuffled")
        }.rest().queue()
    }
}
