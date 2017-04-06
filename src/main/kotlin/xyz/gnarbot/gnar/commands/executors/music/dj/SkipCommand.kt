package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Message
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Category
import xyz.gnarbot.gnar.commands.handlers.Command

@Command(aliases = arrayOf("skip"),
        description = "Skip the current music track.",
        category = Category.MUSIC,
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class SkipCommand : MusicExecutor() {
    override fun execute(message: Message, args: List<String>) {
        val manager = servlet.musicManager

        if (manager.scheduler.queue.isEmpty()) {
            servlet.resetMusicManager()
        } else {
            manager.scheduler.nextTrack()
        }

        message.respond().embed("Skip Current Track") {
            color = musicColor
            description = "The track was skipped."
        }.rest().queue()
    }
}
