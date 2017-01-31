package xyz.gnarbot.gnar.commands.executors.music.dj

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("stop"), clearance = Clearance.DJ)
class StopCommand : MusicExecutor() {
    override fun execute(note: Note, args: List<String>, host: Host, manager: MusicManager) {
        manager.scheduler.queue.clear()
        manager.player.stopTrack()
        manager.player.isPaused = false
        note.replyMusic("Playback has been completely stopped and the queue has been cleared.")
    }
}
