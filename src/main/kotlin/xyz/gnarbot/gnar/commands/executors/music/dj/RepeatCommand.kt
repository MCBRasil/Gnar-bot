package xyz.gnarbot.gnar.commands.executors.music.dj

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Clearance
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("restart"), clearance = Clearance.DJ)
class RepeatCommand : MusicExecutor() {
    override fun execute(note: Note, args: Array<String>, host: Host, manager: MusicManager) {
        manager.scheduler.isRepeating = !manager.scheduler.isRepeating
        note.replyMusic("Music player was set to __${if (manager.scheduler.isRepeating) "repeat" else "not repeat"}__.")
    }
}
