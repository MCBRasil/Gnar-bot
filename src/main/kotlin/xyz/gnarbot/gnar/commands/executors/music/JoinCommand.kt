package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.exceptions.PermissionException
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("join"), inject = true)
class JoinCommand : MusicExecutor() {

    @Inject lateinit var host: Host
    @Inject lateinit var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        if (args.isEmpty()) {
            note.error("No channel name was provided to join.")
            return
        }

        val name = args.joinToString(" ")

        val chan = host.guild.getVoiceChannelsByName(name, true).firstOrNull() ?: host.guild.getVoiceChannelById(name)

        if (chan == null) {
            note.error("Channel `$name` does not exist.")
            return
        }
        host.guild.audioManager.sendingHandler = manager.sendHandler
        try {
            host.guild.audioManager.openAudioConnection(chan)
            note.replyMusic("Joined channel `${chan.name}`.")
        } catch (e: NullPointerException){
            note.error("Couldn't join the channel for some reason! Try another one!")
        } catch (e: PermissionException) {
            if (e.permission == Permission.VOICE_CONNECT) {
                note.error("Gnar doesn't have permission to join `${chan.name}`.")
            }
        }

    }
}
