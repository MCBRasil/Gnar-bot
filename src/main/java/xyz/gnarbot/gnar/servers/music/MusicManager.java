package xyz.gnarbot.gnar.servers.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import xyz.gnarbot.gnar.servers.Servlet;

public class MusicManager {
    public final AudioPlayerManager playerManager;

    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;

    /**
     * Track scheduler for the player.
     */
    public final TrackScheduler scheduler;

    /**
     * Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public final AudioPlayerSendHandler sendHandler;
    /**
     * Voting Cooldown
     */
    public Long lastVoteTime = 0L;
    /**
     * Boolean to check whether there is a vote to skip the song or not
     */
    private boolean votingToSkip = false;

    /**
     * Creates a player and a track scheduler.
     *
     * @param playerManager Audio player playerManager to use for creating the player.
     */
    public MusicManager(Servlet servlet, AudioPlayerManager playerManager) {
        this.playerManager = playerManager;
        player = playerManager.createPlayer();
        scheduler = new TrackScheduler(servlet, player);
        sendHandler = new AudioPlayerSendHandler(player);
        player.addListener(scheduler);
    }

    public boolean isVotingToSkip() {
        return votingToSkip;
    }

    public void setVotingToSkip(boolean votingToSkip) {
        System.out.println("changed voting status");
        this.votingToSkip = votingToSkip;
    }

    public Long getLastVoteTime() {
        return lastVoteTime;
    }

    public void setLastVoteTime(Long lastVoteTime) {
        this.lastVoteTime = lastVoteTime;
    }
}
