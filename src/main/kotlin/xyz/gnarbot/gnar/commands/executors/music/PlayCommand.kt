package xyz.gnarbot.gnar.commands.executors.music

import com.mashape.unirest.http.Unirest
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.executors.music.parent.MusicExecutor
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.servers.music.MusicManager
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("play"))
class PlayCommand : MusicExecutor() {
    override fun execute(note: Note, args: Array<String>, host: Host, manager: MusicManager) {
        if (args.isEmpty()) {
            if (manager.player.isPaused)  {
                manager.player.isPaused = false
                note.replyMusic(msg = "Music is now playing.")
            } else if (manager.player.playingTrack != null) {
                note.error("Music is already playing.")
            } else if (manager.scheduler.queue.isEmpty()) {
                note.replyMusic(msg = "There is no music queued right now. Add some songs with `play -song|url`.")
            }
            return
        }

        if (args[0].contains("https:://")
                && args[0].contains("yout")
                    || args[0].contains("http://")
                    && args[0].contains("yout")
                    || args[0].contains("vimeo")
                    || args[0].contains("twitch.tv")
                    || args[0].contains("soundcloud.com")) {
            loadAndPlay(note, manager, args[0], false)
            return
        }
        val query = args.joinToString("+")

        val jsonObject = Unirest.get("https://www.googleapis.com/youtube/v3/search")
                .queryString("part", "snippet")
                .queryString("maxResults", 1)
                .queryString("type", "video")
                .queryString("q", query)
                .queryString("key", Bot.authTokens["youtube"])
                .asJson()
                .body
                .`object`

        if (jsonObject.length() == 0) {
            note.error("No search results for `${query.replace('+', ' ')}`.")
        }

        val items = jsonObject.getJSONArray("items")

        if (items.length() < 1) {
            note.error("Nothing returned for `${query.replace('+', ' ')}`.")
            return
        }

        val item = items.getJSONObject(0)
        val videoID = item
                .getJSONObject("id")
                .getString("videoId")

        val url = "https://www.youtube.com/watch?v=$videoID"

        loadAndPlay(note, manager, url, false)
    }
}
