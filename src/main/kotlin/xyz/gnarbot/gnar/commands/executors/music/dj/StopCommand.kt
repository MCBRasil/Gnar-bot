package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("stop"),
        description = "Stop and clear the music player.",
        symbol = "♬",
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class StopCommand : MusicExecutor() {

    override fun execute(note: Note, args: List<String>) {
        val manager = servlet.musicManager

        manager.scheduler.queue.clear()
        manager.player.stopTrack()
        manager.player.isPaused = false
        servlet.audioManager.closeAudioConnection()

        note.respond().embed("Stop Playback") {
            color = musicColor
            description = "Playback has been completely stopped and the queue has been cleared."
        }.rest().queue()
    }
}
