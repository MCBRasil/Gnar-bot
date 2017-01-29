package xyz.gnarbot.gnar.commands.executors.music.dj

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("pause"), clearance = Clearance.DJ)
class PauseCommand : MusicExecutor() {
    override fun execute(note: Note, args: Array<String>, host: Host, manager: MusicManager) {
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
