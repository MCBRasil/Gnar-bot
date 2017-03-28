package xyz.gnarbot.gnar.commands.executors.music.dj

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("shuffle"),
        level = Level.DJ,
        description = "Shuffle the music queue.",
        symbol = "♬")
class ShuffleCommand : MusicExecutor() {

    override fun execute(note: Note, args: List<String>) {
        servlet.musicManager.scheduler.shuffle()

        note.respond().embed("Shuffle Queue") {
            color = musicColor
            description = "Player has been shuffled"
        }.rest().queue()
    }
}
