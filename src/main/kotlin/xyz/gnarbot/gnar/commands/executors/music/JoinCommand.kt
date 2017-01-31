package xyz.gnarbot.gnar.commands.executors.music

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("join"))
class JoinCommand : MusicExecutor() {
    override fun execute(note: Note, args: List<String>, host: Host, manager: MusicManager) {
        if (args.isEmpty()) {
            note.error("No channel name was provided to join.")
            return
        }

        val name = args.joinToString(" ")

        val chan = host.getVoiceChannelsByName(name, true).firstOrNull()

        if (chan == null) {
            note.error("Channel `$name` does not exist.")
            return
        }

        host.audioManager.sendingHandler = manager.sendHandler

        try {
            host.audioManager.openAudioConnection(chan)
            note.replyMusic("Joined channel `${chan.name}`.")
        } catch (e: PermissionException) {
            if (e.permission == Permission.VOICE_CONNECT) {
                note.error("Gnar doesn't have permission to join `${chan.name}`.")
            }
        }

    }
}
