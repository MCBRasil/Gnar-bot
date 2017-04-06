package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("shuffle"),
        description = "Shuffle the music queue.",
        symbol = "♬",
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class ShuffleCommand : MusicExecutor() {

    override fun execute(note: Note, args: List<String>) {
        servlet.musicManager.scheduler.shuffle()

        note.respond().embed("Shuffle Queue") {
            color = musicColor
            description = "Player has been shuffled"
        }.rest().queue()
    }
}
