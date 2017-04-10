package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import xyz.gnarbot.gnar.commands.Scope

@Command(
        aliases = arrayOf("reset"),
        description = "Completely reset the music player.",
        category = Category.MUSIC,
        scope = Scope.VOICE,
        permissions = arrayOf(Permission.MANAGE_CHANNEL)
)
class ResetCommand : CommandExecutor() {
    override fun execute(message: Message, args: Array<String>) {
        guildData.resetMusicManager()

        message.respond().embed("Reset Music") {
            color = Constants.MUSIC_COLOR
            description = "The player was completely reset."
        }.rest().queue()
    }
}
