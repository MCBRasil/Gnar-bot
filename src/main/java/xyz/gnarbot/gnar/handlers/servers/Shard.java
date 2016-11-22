package xyz.gnarbot.gnar.handlers.servers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import xyz.gnarbot.gnar.commands.TestCommand;
import xyz.gnarbot.gnar.commands.fun.*;
import xyz.gnarbot.gnar.commands.games.GameLookupCommand;
import xyz.gnarbot.gnar.commands.games.LeagueLookupCommand;
import xyz.gnarbot.gnar.commands.games.OverwatchLookupCommand;
import xyz.gnarbot.gnar.commands.general.*;
import xyz.gnarbot.gnar.commands.media.*;
import xyz.gnarbot.gnar.commands.mod.BanCommand;
import xyz.gnarbot.gnar.commands.mod.DeleteMessagesCommand;
import xyz.gnarbot.gnar.commands.mod.KickCommand;
import xyz.gnarbot.gnar.commands.mod.UnbanCommand;
import xyz.gnarbot.gnar.commands.polls.PollCommand;
import xyz.gnarbot.gnar.handlers.commands.CommandDistributor;
import xyz.gnarbot.gnar.textadventure.AdventureCommand;
import xyz.gnarbot.gnar.textadventure.StartAdventureCommand;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Individual shard instances of the bot.
 */
public class Shard
{
    private final int id;
    private final JDA jda;
    
    private final CommandDistributor distributor = new CommandDistributor();
    // LinkedHashMap access is faster. Pre-allocate to waste less CPU cycles.
    private final Map<Guild, Host> hosts = new LinkedHashMap<>(1000);
    
    public Shard(int id, JDA jda)
    {
        this.id = id;
        this.jda = jda;
        
        jda.addEventListener(new ShardListener(this));
    
        //distributor.registerAll("xyz.gnarbot.gnar.commands");

        //General Commands
        distributor.register(HelpCommand.class);
        distributor.register(InviteBotCommand.class);
        distributor.register(PingCommand.class);
        distributor.register(MathCommand.class);
        distributor.register(RemindMeCommand.class);
        distributor.register(GoogleCommand.class);
        distributor.register(YoutubeCommand.class);
        distributor.register(UptimeCommand.class);
        distributor.register(WhoIsCommand.class);
        distributor.register(BotInfoCommand.class);
        //End General Commands

        //Fun Commands
        distributor.register(ASCIICommand.class);
        distributor.register(CoinFlipCommand.class);
        distributor.register(DialogCommand.class);
        distributor.register(YodaTalkCommand.class);
        distributor.register(RollCommand.class);
        distributor.register(PoopCommand.class);
        distributor.register(GoodShitCommand.class);
        distributor.register(EightBallCommand.class);
        distributor.register(LeetifyCommand.class);
        distributor.register(MarvelComics.class);
        distributor.register(ProgressionCommand.class);
        distributor.register(Rule34Command.class);
        distributor.register(ServersSharedCommand.class);
        distributor.register(TextToSpeechCommand.class);
        distributor.register(ReactToMessageCommand.class);
        distributor.register(ChampDataCommand.class);
        distributor.register(DiscordBotsUserInfoCommand.class);
        distributor.register(TriviaAnswerCommand.class);
        distributor.register(TriviaCommand.class);
        distributor.register(GraphCommand.class);
        distributor.register(UrbanDictionaryCommand.class);
        //End Fun Commands

        //Mod Commands
        distributor.register(BanCommand.class);
        distributor.register(KickCommand.class);
        distributor.register(UnbanCommand.class);
        distributor.register(DeleteMessagesCommand.class);
        //End Mod Commands

        //Testing Commands
        distributor.register(TestCommand.class);
        //End Testing Commands

        //Text Adventure Commands
        distributor.register(AdventureCommand.class);
        distributor.register(StartAdventureCommand.class);
        //End Text Adventure Commands

        //Game Commands
        distributor.register(OverwatchLookupCommand.class);
        distributor.register(LeagueLookupCommand.class);
        distributor.register(GameLookupCommand.class);
        //End Game Commands

        //Poll Commands
        distributor.register(PollCommand.class);
        //End Poll Commands

        //Media Commands
        distributor.register(CatsCommand.class);
        distributor.register(ExplosmCommand.class);
        distributor.register(ExplosmRCGCommand.class);
        distributor.register(GarfieldCommand.class);
        distributor.register(XKCDCommand.class);
        //End Media Commands
    }
    
    /**
     * Returns ID of the shard.
     * @return ID of the shard.
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Lazily get a Host instance from a Guild instance.
     *
     * @see Host
     * @param guild JDA Guild.
     * @return Host instance of Guild.
     */
    public Host getHost(Guild guild)
    {
        if (guild == null) return null;
            
        Host host = hosts.get(guild);
        
        if (host == null)
        {
            host = new Host(this, guild);
            hosts.put(guild, host);
        }
        
        return host;
    }
    
    /**
     * Returns the Hosts instances of this shard.
     * @return The Hosts instances of this shard.
     */
    public List<Host> getHosts()
    {
        return new ArrayList<>(hosts.values());
    }
    
    /**
     * Returns the JDA API of this shard.
     * @return The JDA API of this shard.
     */
    public JDA getJDA()
    {
        return jda;
    }
    
    /**
     * Returns the command distribution handler.
     * @return Command distribution handler.
     */
    public CommandDistributor getDistributor()
    {
        return distributor;
    }
    
    /**
     * @return The string representation of the shard.
     */
    @Override
    public String toString()
    {
        return "Shard(id="+id+", guilds="+jda.getGuilds().size()+")";
    }
}

