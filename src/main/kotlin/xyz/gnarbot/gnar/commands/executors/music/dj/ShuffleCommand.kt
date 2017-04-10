package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.Constants
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.CommandExecutor
import xyz.gnarbot.gnar.commands.Scope

@Command(
        aliases = arrayOf("shuffle"),
        description = "Shuffle the music queue.",
        category = Category.MUSIC,
        scope = Scope.VOICE,
        permissions = arrayOf(Permission.MANAGE_CHANNEL)
)
class ShuffleCommand : CommandExecutor() {

    override fun execute(message: Message, args: Array<String>) {
        guildData.musicManager.scheduler.shuffle()

        message.respond().embed("Shuffle Queue") {
            color = Constants.MUSIC_COLOR
            description = "Player has been shuffled"
        }.rest().queue()
    }
}
