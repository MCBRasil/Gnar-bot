package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command

@Command(aliases = arrayOf("stop"),
        description = "Stop and clear the music player.",
        category = Category.MUSIC,
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class StopCommand : MusicExecutor() {

    override fun execute(message: Message, args: List<String>) {
        val manager = servlet.musicManager

        manager.scheduler.queue.clear()
        manager.player.stopTrack()
        manager.player.isPaused = false
        servlet.audioManager.closeAudioConnection()

        message.respond().embed("Stop Playback") {
            color = musicColor
            description = "Playback has been completely stopped and the queue has been cleared."
        }.rest().queue()
    }
}
