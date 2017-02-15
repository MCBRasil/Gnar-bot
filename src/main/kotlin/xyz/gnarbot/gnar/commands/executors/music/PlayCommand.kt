package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
import net.dv8tion.jda.core.entities.SelfUser
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.YouTube

@Command(aliases = arrayOf("play"),
        usage = "-(url|YT search)",
        description = "Joins and play music in a channel.")
class PlayCommand : MusicExecutor() {
    @Inject lateinit private var selfUser : SelfUser
    @Inject lateinit private var host : Host
    @Inject lateinit private var manager : MusicManager

    override fun execute(note: Note, args: List<String>) {
        val botChannel = host.personHandler.asPerson(selfUser).voiceChannel
        val userChannel = note.author.voiceChannel

        if (botChannel != null && botChannel != userChannel) {
            note.error("The bot is already playing music in another channel.")
            return
        }

        if (userChannel == null) {
            note.error("You must be in a voice channel to play music.")
            return
        }

        if (args.isEmpty()) {
            if (manager.player.isPaused) {
                manager.player.isPaused = false
                note.replyMusic(msg = "Music is now playing.")
            } else if (manager.player.playingTrack != null) {
                note.error("Music is already playing.")
            } else if (manager.scheduler.queue.isEmpty()) {
                note.replyMusic(msg = "There is no music queued right now. Add some songs with `play -song|url`.")
            }
            return
        }

        val url = if (args[0].contains("https://")
                || args[0].contains("http://")
                && args[0].contains("yout")
                || args[0].contains("vimeo")
                || args[0].contains("twitch.tv")
                || args[0].contains("soundcloud.com")) {
            args[0]
        } else {
            val query = args.joinToString("+")

            val results = YouTube.search(query, 1)

            if (results.isEmpty()) {
                note.error("No YouTube results returned for `${query.replace('+', ' ')}`.")
                return
            }

            val result = results[0]
            val videoID = result.id
            "https://www.youtube.com/watch?v=$videoID"
        }
        
        if (botChannel == null) {
            host.audioManager.sendingHandler = manager.sendHandler
            host.audioManager.openAudioConnection(userChannel)
            note.replyMusic("Joined channel `${userChannel.name}`.")
        }

        loadAndPlay(note, manager, url)
    }
}
