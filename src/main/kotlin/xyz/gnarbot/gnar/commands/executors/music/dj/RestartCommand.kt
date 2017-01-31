package xyz.gnarbot.gnar.commands.executors.music.dj

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("restart"), clearance = Clearance.DJ)
class RestartCommand : MusicExecutor() {
    override fun execute(note: Note, args: List<String>, host: Host, manager: MusicManager) {
        var track = manager.player.playingTrack

        if (track == null) {
            track = manager.scheduler.lastTrack
        }

        if (track != null) {
            note.replyMusic("Restarting track: `${track.info.title}`.")
            manager.player.playTrack(track.makeClone())
        } else {
            note.error("No track has been previously started.")
        }
    }
}
