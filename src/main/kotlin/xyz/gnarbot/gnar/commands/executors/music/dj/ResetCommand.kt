package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("reset"),
        description = "Completely reset the music player.",
        symbol = "♬",
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class ResetCommand : MusicExecutor() {
    override fun execute(note: Note, args: List<String>) {
        servlet.resetMusicManager()

        note.respond().embed("Reset Music") {
            color = musicColor
            description = "The player was completely reset."
        }.rest().queue()
    }
}
