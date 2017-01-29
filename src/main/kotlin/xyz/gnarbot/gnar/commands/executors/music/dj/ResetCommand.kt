package xyz.gnarbot.gnar.commands.executors.music.dj

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("reset"), clearance = Clearance.DJ)
class ResetCommand : MusicExecutor() {
    override fun execute(note: Note, args: Array<String>, host: Host, manager: MusicManager) {
        manager.scheduler.queue.clear()
        manager.player.destroy()
        host.audioManager.sendingHandler = null
        host.setMusicManager(null)

        val _manager = host.getMusicManager()
        host.audioManager.sendingHandler = _manager.sendHandler
        note.replyMusic("The player has been completely reset.")
    }
}
