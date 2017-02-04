package xyz.gnarbot.gnar.commands.executors.music

import com.google.inject.Inject
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note
import xyz.gnarbot.gnar.utils.YouTube

@Command(aliases = arrayOf("play"))
class PlayCommand : MusicExecutor() {

    @Inject lateinit private var manager: MusicManager

    override fun execute(note: Note, args: List<String>) {
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

        if (args[0].contains("https://")
                || args[0].contains("http://")
                && args[0].contains("yout")
                || args[0].contains("vimeo")
                || args[0].contains("twitch.tv")
                || args[0].contains("soundcloud.com")) {
            loadAndPlay(note, manager, args[0])
            return
        }

        val query = args.joinToString("+")

        val results = YouTube.search(query, 1)

        if (results.isEmpty()) {
            note.error("Nothing returned for `${query.replace('+', ' ')}`.")
            return
        }

        val result = results[0]
        val videoID = result.id
        val url = "https://www.youtube.com/watch?v=$videoID"

        loadAndPlay(note, manager, url)
    }
}
