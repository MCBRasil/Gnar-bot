package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
import net.dv8tion.jda.core.entities.SelfUser
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.Servlet
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.YouTube

@Command(aliases = arrayOf("play"),
        usage = "-(url|YT search)",
        description = "Joins and play music in a channel.",
        symbol = "♬")
class PlayCommand : MusicExecutor() {
    @Inject lateinit private var selfUser: SelfUser
    @Inject lateinit private var servlet: Servlet
    @Inject lateinit private var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
        val botChannel = servlet.clientHandler.getClient(selfUser)!!.voiceChannel
        val userChannel = note.author.voiceChannel

        if (botChannel != null && botChannel != userChannel) {
            note.respond().error("The bot is already playing music in another channel.").queue()
            return
        }

        if (userChannel == null) {
            note.respond().error("You must be in a voice channel to play music.").queue()
            return
        }

        if (args.isEmpty()) {
            if (manager.player.isPaused) {
                manager.player.isPaused = false
                note.respond().embed("Play Music") {
                    color = musicColor
                    description = "Music is now playing."
                }.rest().queue()
            } else if (manager.player.playingTrack != null) {
                note.respond().error("Music is already playing.").queue()
            } else if (manager.scheduler.queue.isEmpty()) {
                note.respond().embed("Empty Queue") {
                    color = musicColor
                    description = "There is no music queued right now. Add some songs with `play -song|url`."
                }.rest().queue()
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
                note.respond().error("No YouTube results returned for `${query.replace('+', ' ')}`.").queue()
                return
            }

            val result = results[0]
            val videoID = result.id
            "https://www.youtube.com/watch?v=$videoID"
        }

        if (botChannel == null) {
            servlet.audioManager.sendingHandler = manager.sendHandler
            servlet.audioManager.openAudioConnection(userChannel)

            note.respond().embed("Music Playback") {
                color = musicColor
                description = "Joined channel `${userChannel.name}`."
            }.rest().queue()
        }

        manager.loadAndPlay(note, url)
    }
}
