package xyz.gnarbot.gnar.commands.executors.music.dj

import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.Level
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("leave"),
        level = Level.DJ,
        description = "Leave the current music channel but keep the queue intact.",
        symbol = "♬")
class LeaveCommand : MusicExecutor() {
    override fun execute(note: Note, args: List<String>) {
        servlet.audioManager.sendingHandler = null
        servlet.audioManager.closeAudioConnection()
    }
}
