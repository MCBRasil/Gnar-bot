package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("stop"), level = Level.DJ)
class StopCommand : MusicExecutor() {

    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        manager.scheduler.queue.clear()
        manager.player.stopTrack()
        manager.player.isPaused = false
        note.replyMusic("Playback has been completely stopped and the queue has been cleared.")
    }
}
