package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import xyz.gnarbot.gnar.commands.Scope

@Command(
        aliases = arrayOf("leave"),
        description = "Leave the current music channel but keep the queue intact.",
        category = Category.MUSIC,
        scope = Scope.VOICE,
        permissions = arrayOf(Permission.MANAGE_CHANNEL)
)
class LeaveCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        guild.audioManager.sendingHandler = null
        guild.audioManager.closeAudioConnection()
    }
}
