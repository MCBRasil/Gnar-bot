package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command

@Command(aliases = arrayOf("reset"),
        description = "Completely reset the music player.",
        category = Category.MUSIC,
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class ResetCommand : MusicExecutor() {
    override fun execute(message: Message, args: List<String>) {
        guildData.resetMusicManager()

        message.respond().embed("Reset Music") {
            color = musicColor
            description = "The player was completely reset."
        }.rest().queue()
    }
}
