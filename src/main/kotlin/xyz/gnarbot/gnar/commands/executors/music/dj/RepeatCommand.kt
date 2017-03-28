package xyz.gnarbot.gnar.commands.executors.music.dj

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("repeat"),
        level = Level.DJ,
        description = "Set if the music player should repeat.",
        symbol = "♬")
class RepeatCommand : MusicExecutor() {

    override fun execute(note: Note, args: List<String>) {
        val manager = servlet.musicManager

        manager.scheduler.isRepeating = !manager.scheduler.isRepeating

        note.respond().embed("Repeat Queue") {
            color = musicColor
            description = "Music player was set to __${if (manager.scheduler.isRepeating) "repeat" else "not repeat"}__."
        }.rest().queue()
    }
}
