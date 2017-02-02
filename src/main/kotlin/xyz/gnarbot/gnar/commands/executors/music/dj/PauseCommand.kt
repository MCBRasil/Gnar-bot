package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.BotPermission
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("pause"), botPermission = BotPermission.DJ, inject = true)
class PauseCommand : MusicExecutor() {
    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        if (manager.player.playingTrack == null) {
            note.error("Can not pause or resume player because there is no track loaded for playing.")
            return
        }

        manager.player.isPaused = !manager.player.isPaused

        if (manager.player.isPaused) {
            note.replyMusic("The player has been paused.")
        } else {
            note.replyMusic("The player has resumed playing.")
        }
    }
}
