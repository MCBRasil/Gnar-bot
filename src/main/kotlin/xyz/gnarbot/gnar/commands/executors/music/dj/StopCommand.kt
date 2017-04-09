package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor

@Command(
        aliases = arrayOf("stop"),
        description = "Stop and clear the music player.",
        category = Category.MUSIC,
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL)
)
class StopCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        val manager = guildData.musicManager

        manager.scheduler.queue.clear()
        manager.player.stopTrack()
        manager.player.isPaused = false
        guild.audioManager.closeAudioConnection()

        message.respond().embed("Stop Playback") {
            color = Constants.MUSIC_COLOR
            description = "Playback has been completely stopped and the queue has been cleared."
        }.rest().queue()
    }
}
