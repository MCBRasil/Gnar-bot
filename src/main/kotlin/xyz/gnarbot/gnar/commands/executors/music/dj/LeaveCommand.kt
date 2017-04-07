package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command

@Command(aliases = arrayOf("leave"),
        description = "Leave the current music channel but keep the queue intact.",
        category = Category.MUSIC,
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class LeaveCommand : MusicExecutor() {
    override fun execute(message: Message, args: List<String>) {
        guild.audioManager.sendingHandler = null
        guild.audioManager.closeAudioConnection()
    }
}
