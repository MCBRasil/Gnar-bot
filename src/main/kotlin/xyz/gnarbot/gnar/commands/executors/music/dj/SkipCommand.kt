package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("skip"),
        description = "Skip the current music track.",
        symbol = "♬",
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class SkipCommand : MusicExecutor() {
    override fun execute(note: Note, args: List<String>) {
        val manager = servlet.musicManager

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
