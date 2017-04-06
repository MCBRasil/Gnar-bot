package xyz.gnarbot.gnar.commands.executors.music.dj

import net.dv8tion.jda.core.Permission
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("pause"),
        description = "Pause or resume the music player.",
        symbol = "♬",
        voicePermissions = arrayOf(Permission.MANAGE_CHANNEL))
class PauseCommand : MusicExecutor() {

    override fun execute(note: Note, args: List<String>) {
        val manager = servlet.musicManager

        if (manager.player.playingTrack == null) {
            note.respond().error("Can not pause or resume player because there is no track loaded for playing.")
            return
        }

        manager.player.isPaused = !manager.player.isPaused

        note.respond().embed("Playback Control") {
            color = musicColor
            description = if (manager.player.isPaused) {
                "The player has been paused."
            } else {
                "The player has resumed playing."
            }
        }.rest().queue()
    }
}
