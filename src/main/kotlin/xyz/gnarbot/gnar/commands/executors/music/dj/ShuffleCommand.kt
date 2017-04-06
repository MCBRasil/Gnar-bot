package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command

@Command(aliases = arrayOf("shuffle"),
        description = "Shuffle the music queue.",
        category = Category.MUSIC,
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class ShuffleCommand : MusicExecutor() {

    override fun execute(message: Message, args: List<String>) {
        servlet.musicManager.scheduler.shuffle()

        message.respond().embed("Shuffle Queue") {
            color = musicColor
            description = "Player has been shuffled"
        }.rest().queue()
    }
}
