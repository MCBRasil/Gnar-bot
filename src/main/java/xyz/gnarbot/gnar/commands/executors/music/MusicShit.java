package xyz.gnarbot.gnar.commands.executors.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import xyz.gnarbot.gnar.utils.KUtils;

import java.util.*;
import java.util.logging.Level;

public class MusicShit extends ListenerAdapter {
    public static final int DEFAULT_VOLUME = 35; //(0 - 150, where 100 is default max volume)

    private final AudioPlayerManager playerManager;
    private final Map<String, GuildMusicManager> musicManagers;

    public MusicShit()
    {
        java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);

        this.playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());

        musicManagers = new HashMap<String, GuildMusicManager>();
    }

    //Prefix for all commands: .
    //Example:  .play
    //Current commands
    // join [name]  - Joins a voice channel that has the provided name
    // join [id]    - Joins a voice channel based on the provided id.
    // leave        - Leaves the voice channel that the bot is currently in.
    // play         - Plays songs from the current queue. Starts playing again if it was previously paused
    // play [url]   - Adds a new song to the queue and starts playing if it wasn't playing already
    // pplay        - Adds a playlist to the queue and starts playing if not already playing
    // pause        - Pauses audio playback
    // stop         - Completely stops audio playback, skipping the current song.
    // skip         - Skips the current song, automatically starting the next
    // nowplaying   - Prints information about the currently playing song (title, current time)
    // np           - alias for nowplaying
    // list         - Lists the songs in the queue
    // volume [val] - Sets the volume of the MusicPlayer [10 - 100]
    // restart      - Restarts the current song or restarts the previous song if there is no current song playing.
    // repeat       - Makes the player repeat the currently playing song
    // reset        - Completely resets the player, fixing all errors and clearing the queue.
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {

        String[] command = event.getMessage().getContent().split(" ", 2);
        if (!command[0].startsWith("_")) {    //message doesn't start with prefix.
            return;
        }

        boolean isDJ = false;

        Role dj = event.getGuild().getRolesByName("DJ", true).get(0);

        if(!event.getAuthor().getId().equals("138481382794985472") || !event.getAuthor().getId().equals("192113152328990726")) {
            isDJ = true;
        }

        if(dj!=null) {
            if(event.getGuild().getMember(event.getAuthor()).getRoles().contains(dj)) {
                isDJ = true;
            }
        }

        Guild guild = event.getGuild();
        GuildMusicManager mng = getMusicManager(guild);
        AudioPlayer player = mng.player;
        TrackScheduler scheduler = mng.scheduler;

        if ("_join".equals(command[0]))
        {
            if (command.length == 1) //No channel name was provided to search for.
            {
                event.getChannel().sendMessage("No channel name was provided to search with to join.").queue();
            }
            else
            {
                VoiceChannel chan = guild.getVoiceChannelById(command[1]);
                if (chan == null)
                    chan = guild.getVoiceChannelsByName(command[1], true).stream().findFirst().orElse(null);
                if (chan == null)
                {
                    event.getChannel().sendMessage("I don't see where  " + command[1] + " is. Did you make a typo?").queue();
                }
                else
                {
                    guild.getAudioManager().setSendingHandler(mng.sendHandler);

                    try
                    {
                        guild.getAudioManager().openAudioConnection(chan);
                    }
                    catch (PermissionException e)
                    {
                        if (e.getPermission() == Permission.VOICE_CONNECT)
                        {
                            event.getChannel().sendMessage("GNAR doesn't have permission to join this channel " + chan.getName()).queue();
                        }
                    }
                }
            }
        }
        else if ("_leave".equals(command[0]))
        {
            if(isDJ) {
                guild.getAudioManager().setSendingHandler(null);
                guild.getAudioManager().closeAudioConnection();
            } else {
                event.getChannel().sendMessage("You must have the role `DJ` to run this command!");
            }
        }
        else if ("_play".equals(command[0]))
        {
            if (command.length == 1) //It is only the command to start playback (probably after pause)
            {
                if (player.isPaused())
                {
                    player.setPaused(false);
                    event.getChannel().sendMessage("Music is now playing.").queue();
                }
                else if (player.getPlayingTrack() != null)
                {
                    event.getChannel().sendMessage("Music is already playing!").queue();
                }
                else if (scheduler.queue.isEmpty())
                {
                    event.getChannel().sendMessage("There is no music queued right now. Add some songs with _play [song]").queue();
                }
            }
            else    //Commands has 2 parts, .play and url.
            {
                if(command[1].contains("http") && command[1].contains("yout")) {
                    loadAndPlay(mng, event.getChannel(), command[1], false);
                } else {
                    String[] args = Arrays.copyOfRange(command, 1, command.length);
                    loadAndPlay(mng, event.getChannel(), KUtils.getFirstVideo(args), false);
                }
            }
        }
        else if ("_pplay".equals(command[0]) && command.length == 2)
        {
            if(isDJ) {
                loadAndPlay(mng, event.getChannel(), command[1], true);
            } else {
                event.getChannel().sendMessage("You must have the role `DJ` to run this command!");
            }
        }
        else if ("_skip".equals(command[0]))
        {
            if(isDJ) {
                scheduler.nextTrack();
                event.getChannel().sendMessage("The current track was skipped.").queue();
            } else {
                event.getChannel().sendMessage("You must have the role `DJ` to run this command!");
            }
        }
        else if ("_pause".equals(command[0]))
        {
            if(isDJ) {
                if (player.getPlayingTrack() == null) {
                    event.getChannel().sendMessage("Cannot pause or resume player because no track is loaded for playing.").queue();
                    return;
                }

                player.setPaused(!player.isPaused());
                if (player.isPaused())
                    event.getChannel().sendMessage("The player has been paused.").queue();
                else
                    event.getChannel().sendMessage("The player has resumed playing.").queue();
            } else {
                event.getChannel().sendMessage("You must have the role `DJ` to run this command!");
            }
        }
        else if ("_stop".equals(command[0]))
        {
            if(isDJ) {
                scheduler.queue.clear();
                player.stopTrack();
                player.setPaused(false);
                event.getChannel().sendMessage("Playback has been completely stopped and the queue has been cleared.").queue();
            }  else {
                event.getChannel().sendMessage("You must have the role `DJ` to run this command!");
            }
        }
        else if ("_volume".equals(command[0]))
        {
            if(isDJ) {
                if (command.length == 1) {
                    event.getChannel().sendMessage("Current player volume: **" + player.getVolume() + "**").queue();
                } else {
                    try {
                        int newVolume = Math.max(10, Math.min(100, Integer.parseInt(command[1])));
                        int oldVolume = player.getVolume();
                        player.setVolume(newVolume);
                        event.getChannel().sendMessage("Player volume changed from `" + oldVolume + "` to `" + newVolume + "`").queue();
                    } catch (NumberFormatException e) {
                        event.getChannel().sendMessage("`" + command[1] + "` is not a valid integer. (10 - 100)").queue();
                    }
                }
            } else {
                event.getChannel().sendMessage("You must have the role `DJ` to run this command!");
            }
        }
        else if ("_restart".equals(command[0]))
        {
            if(isDJ) {
                AudioTrack track = player.getPlayingTrack();
                if (track == null)
                    track = scheduler.lastTrack;

                if (track != null) {
                    event.getChannel().sendMessage("Restarting track: " + track.getInfo().title).queue();
                    player.playTrack(track.makeClone());
                } else {
                    event.getChannel().sendMessage("No track has been previously started, so the player cannot replay a track!").queue();
                }
            } else {
                event.getChannel().sendMessage("You must have the role `DJ` to run this command!");
            }
        }
        else if ("_repeat".equals(command[0]))
        {
            if(isDJ) {
                scheduler.setRepeating(!scheduler.isRepeating());
                event.getChannel().sendMessage("Player was set to: **" + (scheduler.isRepeating() ? "repeat" : "not repeat") + "**").queue();
            } else {
                event.getChannel().sendMessage("You must have the role `DJ` to run this command!");
            }
        }
        else if ("_reset".equals(command[0]))
        {
            if(isDJ) {
                synchronized (musicManagers) {
                    scheduler.queue.clear();
                    player.destroy();
                    guild.getAudioManager().setSendingHandler(null);
                    musicManagers.remove(guild.getId());
                }

                mng = getMusicManager(guild);
                guild.getAudioManager().setSendingHandler(mng.sendHandler);
                event.getChannel().sendMessage("The player has been completely reset!").queue();
            } else {
                event.getChannel().sendMessage("You must have the role `DJ` to run this command!");
            }
        }
        else if ("_nowplaying".equals(command[0]) || ".np".equals(command[0]))
        {
            AudioTrack currentTrack = player.getPlayingTrack();
            if (currentTrack != null)
            {
                String title = currentTrack.getInfo().title;
                String position = getTimestamp(currentTrack.getPosition());
                String duration = getTimestamp(currentTrack.getDuration());

                String nowplaying = String.format("**Playing:** %s\n**Time:** [%s / %s]",
                        title, position, duration);

                event.getChannel().sendMessage(nowplaying).queue();
            }
            else
                event.getChannel().sendMessage("The player is not currently playing anything!").queue();
        }
        else if ("_list".equals(command[0]))
        {
            Queue<AudioTrack> queue = scheduler.queue;
            synchronized (queue)
            {
                if (queue.isEmpty())
                {
                    event.getChannel().sendMessage("The queue is currently empty!").queue();
                }
                else
                {
                    int trackCount = 0;
                    long queueLength = 0;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Current Queue: Entries: ").append(queue.size()).append("\n");
                    for (AudioTrack track : queue)
                    {
                        queueLength += track.getDuration();
                        if (trackCount < 10)
                        {
                            sb.append("`[").append(getTimestamp(track.getDuration())).append("]` ");
                            sb.append(track.getInfo().title).append("\n");
                            trackCount++;
                        }
                    }
                    sb.append("\n").append("Total Queue Time Length: ").append(getTimestamp(queueLength));

                    event.getChannel().sendMessage(sb.toString()).queue();
                }
            }
        }
        else if ("_shuffle".equals(command[0]))
        {
            if(isDJ) {
                if (scheduler.queue.isEmpty()) {
                    event.getChannel().sendMessage("The queue is currently empty!").queue();
                    return;
                }

                scheduler.shuffle();
                event.getChannel().sendMessage("The queue has been shuffled!").queue();
            } else {
                event.getChannel().sendMessage("You must have the role `DJ` to run this command!");
            }
        }
    }

    private void loadAndPlay(GuildMusicManager mng, final MessageChannel channel, final String trackUrl, final boolean addPlaylist)
    {
        playerManager.loadItemOrdered(mng, trackUrl, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                String msg = "Adding to queue: " + track.getInfo().title;
                if (mng.player.getPlayingTrack() == null)
                    msg += "\nand the Player has started playing;";

                mng.scheduler.queue(track);
                channel.sendMessage(msg).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                List<AudioTrack> tracks = playlist.getTracks();


                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                if (addPlaylist)
                {
                    channel.sendMessage("Adding **" + playlist.getTracks().size() +"** tracks to queue from playlist: " + playlist.getName()).queue();
                    tracks.forEach(mng.scheduler::queue);
                }
                else
                {
                    channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
                    mng.scheduler.queue(firstTrack);
                }
            }

            @Override
            public void noMatches()
            {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception)
            {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    private GuildMusicManager getMusicManager(Guild guild)
    {
        String guildId = guild.getId();
        GuildMusicManager mng = musicManagers.get(guildId);
        if (mng == null)
        {
            synchronized (musicManagers)
            {
                mng = musicManagers.get(guildId);
                if (mng == null)
                {
                    mng = new GuildMusicManager(playerManager);
                    mng.player.setVolume(DEFAULT_VOLUME);
                    musicManagers.put(guildId, mng);
                }
            }
        }
        return mng;
    }

    private static String getTimestamp(long milliseconds)
    {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        if (hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else
            return String.format("%02d:%02d", minutes, seconds);
    }
}
