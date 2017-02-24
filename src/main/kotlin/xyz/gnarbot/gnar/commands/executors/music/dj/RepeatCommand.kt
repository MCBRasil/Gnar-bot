package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("repeat"),
        level = Level.DJ,
        description = "Set if the music player should repeat.")
class RepeatCommand : MusicExecutor() {

    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        manager.scheduler.isRepeating = !manager.scheduler.isRepeating

        note.embed("Repeat Queue") {
            color(musicColor)
            description("Music player was set to __${if (manager.scheduler.isRepeating) "repeat" else "not repeat"}__.")
        }.rest().queue()
    }
}
