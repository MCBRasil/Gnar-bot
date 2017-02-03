package xyz.gnarbot.gnar.servers;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.CommandHandler;
import xyz.gnarbot.gnar.members.Person;
import xyz.gnarbot.gnar.members.PersonHandler;
import xyz.gnarbot.gnar.servers.music.MusicManager;

/**
 * Represents a bot on each {@link Guild}.
 * @see Guild
 */
public class Host {
    private final Shard shard;
    private final String id;

    private final PersonHandler personHandler;
    private final CommandHandler commandHandler;
    
    private MusicManager musicManager;
    
    public Host(Shard shard, String id) {
        this.shard = shard;
        this.id = id;
        this.personHandler = new PersonHandler(this);
        this.commandHandler = new CommandHandler(this);
    }

    public void setMusicManager(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    public MusicManager getMusicManager() {
        if (musicManager == null) {
            musicManager = new MusicManager(Bot.INSTANCE.getPlayerManager());
            musicManager.player.setVolume(35);
        }
        return musicManager;
    }

    /**
     * @return Guild instance.
     */
    public Guild getGuild() {
        return shard.getJDA().getGuildById(id);
    }

    public String getId() {
        return id;
    }

    public PersonHandler getPersonHandler() {
        return personHandler;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public Shard getShard() {
        return shard;
    }

    //    @Deprecated("Useless")
//    /** Load JSON instance from the Host's storage. */
//    public boolean loadJSON()
//    {
//        file = Bot.files.hosts.child("$id.json")
//        file.createNewFile()
//
//        val content = file.readText()
////        if (content.isEmpty()) jsonObject = NullableJSON()
////        else jsonObject = NullableJSON(content)
//    }
//
//    @Deprecated("Useless")
//    /** Save the JSON instance of the Host. */
//    public boolean saveJSON() = file.writeText(jsonObject.toString(4))

    /**
     * Attempt to ban the member from the guild.
     * @return If the bot had permission.
     */
    public boolean ban(Person person) {
        try {
            getGuild().getController().ban(((User) person), 2).queue();
            return true;
        } catch (PermissionException e) {
            return false;
        }
    }

    /**
     * Attempt to un-ban the member from the guild.
     * @return If the bot had permission.
     */
    public boolean unban(Person person) {
        try {
            getGuild().getController().unban(person).queue();
            return true;
        } catch (PermissionException e) {
            return false;
        }
    }

    /**
     * Attempt to kick the member from the guild.
     * @return If the bot had permission.
     */
    public boolean kick(Person person) {
        try {
            getGuild().getController().kick(person).queue();
            return true;
        } catch (PermissionException e) {
            return false;
        }
    }

    /**
     * Attempt to mute the member in the guild.
     * @return If the bot had permission.
     */
    public boolean mute(Person person) {
        try {
            getGuild().getController().setMute(person, true).queue();
            return true;
        } catch (PermissionException e) {
            return false;
        }
    }

    /**
     * Attempt to unmute the member in the guild.
     * @return If the bot had permission.
     */
    public boolean unmute(Person person) {
        try {
            getGuild().getController().setMute(person, false).queue();
            return true;
        } catch (PermissionException e) {
            return false;
        }
    }

    /**
     * @return String representation of the Host.
     */
    public String toString() {
        return "Host(id=" + getGuild().getId() + ", shard=" + shard.getId() + ", guild=\"" + getGuild().getName() + "\")";
    }

    public void handleMessage(Message message) {
        Person person = getPersonHandler().asPerson(message.getAuthor());
        getCommandHandler().callCommand(message, message.getContent(), person);
    }
}
