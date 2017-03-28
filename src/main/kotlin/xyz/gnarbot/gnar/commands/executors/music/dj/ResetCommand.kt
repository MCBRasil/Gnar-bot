package xyz.gnarbot.gnar.commands.executors.music.dj

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("reset"),
        level = Level.DJ,
        description = "Completely reset the music player.",
        symbol = "♬")
class ResetCommand : MusicExecutor() {
    override fun execute(note: Note, args: List<String>) {
        servlet.resetMusicManager()

        note.respond().embed("Reset Music") {
            color = musicColor
            description = "The player was completely reset."
        }.rest().queue()
    }
}
