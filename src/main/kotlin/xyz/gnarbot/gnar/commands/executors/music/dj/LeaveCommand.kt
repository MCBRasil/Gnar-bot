package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("leave"), level = Level.DJ, description = "Leave the current music channel but keep the queue intact.")
class LeaveCommand : MusicExecutor() {
    @Inject lateinit private var host: Host

    override fun execute(note: Note, args: List<String>) {
        host.audioManager.sendingHandler = null
        host.audioManager.closeAudioConnection()
    }
}
