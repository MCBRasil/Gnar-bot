package xyz.gnarbot.gnar.commands.executors.music.dj

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.members.BotPermission
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("skip"), botPermission = BotPermission.DJ)
class SkipCommand : MusicExecutor() {

    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        manager.scheduler.nextTrack()
        note.replyMusic("The current track was skipped.")
    }
}
