package xyz.gnarbot.gnar.commands.handlers;

import com.google.inject.Inject;
import xyz.gnarbot.gnar.commands.executors.admin.GarbageCollectCommand;
import xyz.gnarbot.gnar.commands.executors.admin.JavascriptCommand;
import xyz.gnarbot.gnar.commands.executors.admin.ShardInfoCommand;
import xyz.gnarbot.gnar.commands.executors.admin.ThrowError;
import xyz.gnarbot.gnar.commands.executors.fun.*;
import xyz.gnarbot.gnar.commands.executors.games.GameLookupCommand;
import xyz.gnarbot.gnar.commands.executors.games.LeagueLookupCommand;
import xyz.gnarbot.gnar.commands.executors.games.OverwatchLookupCommand;
import xyz.gnarbot.gnar.commands.executors.general.*;
import xyz.gnarbot.gnar.commands.executors.media.*;
import xyz.gnarbot.gnar.commands.executors.mod.*;
import xyz.gnarbot.gnar.commands.executors.music.NowPlayingCommand;
import xyz.gnarbot.gnar.commands.executors.music.PlayCommand;
import xyz.gnarbot.gnar.commands.executors.music.QueueCommand;
import xyz.gnarbot.gnar.commands.executors.music.VoteSkipCommand;
import xyz.gnarbot.gnar.commands.executors.music.dj.*;
import xyz.gnarbot.gnar.commands.executors.polls.PollCommand;
import xyz.gnarbot.gnar.commands.executors.test.TestCommand;
import xyz.gnarbot.gnar.commands.executors.test.TestEmbedCommand;
import xyz.gnarbot.gnar.textadventure.AdventureCommand;
import xyz.gnarbot.gnar.textadventure.StartAdventureCommand;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Registry unique to each Shard.
 */
public class CommandRegistry {
    /**
     * The mapped registry of invoking key to the classes.
     */
    private Map<String, CommandExecutor> commandMap = new LinkedHashMap<>();

    public CommandRegistry() {
        register(HelpCommand.class);
        register(InviteBotCommand.class);
        register(PingCommand.class);
        register(MathCommand.class);
        register(RemindMeCommand.class);
        register(GoogleCommand.class);
        register(YoutubeCommand.class);
        register(UptimeCommand.class);
        register(WhoIsCommand.class);
        register(BotInfoCommand.class);
        //End General Commands

        //Fun Commands
        register(ASCIICommand.class);
        register(CoinFlipCommand.class);
        register(DialogCommand.class);
        register(YodaTalkCommand.class);
        register(RollCommand.class);
        register(PoopCommand.class);
        register(GoodShitCommand.class);
        register(EightBallCommand.class);
        register(LeetifyCommand.class);
        register(MarvelComics.class);
        register(ProgressionCommand.class);
        register(Rule34Command.class);
        register(ServersSharedCommand.class);
        register(TextToSpeechCommand.class);
        register(ReactCommand.class);
        register(ChampDataCommand.class);
        //register(DiscordBotsUserInfoCommand.class);
        register(TriviaAnswerCommand.class);
        register(TriviaCommand.class);
        register(GraphCommand.class);
        register(UrbanDictionaryCommand.class);
        register(ChampQuoteCommand.class);
        register(CleverBotCommand.class);
        register(PandoraBotCommand.class);
        register(MemeCommand.class);
        //End Fun Commands

        //Mod Commands
        register(BanCommand.class);
        register(KickCommand.class);
        register(UnbanCommand.class);
        register(PruneCommand.class);
        register(DeleteMessageCommand.class);
        //End Mod Commands

        //Testing Commands
        register(TestCommand.class);
        //End Testing Commands

        //Text Adventure Commands
        register(AdventureCommand.class);
        register(StartAdventureCommand.class);
        //End Text Adventure Commands

        //Game Commands
        register(OverwatchLookupCommand.class);
        register(LeagueLookupCommand.class);
        register(GameLookupCommand.class);
        //End Game Commands

        //Poll Commands
        register(PollCommand.class);
        //End Poll Commands

        //Media Commands
        register(CatsCommand.class);
        register(ExplosmCommand.class);
        register(ExplosmRCGCommand.class);
        //register(GarfieldCommand.class);
        register(XKCDCommand.class);
        //End Media Commands

        // Administrator commands
        register(GarbageCollectCommand.class);
        register(JavascriptCommand.class);
        register(ShardInfoCommand.class);
        register(ThrowError.class);

        // Test Commands
        register(TestEmbedCommand.class);
        register(QuoteCommand.class);
        register(TextToBrickCommand.class);

        //MUSIC COMMAND
        register(PlayCommand.class);
        register(LeaveCommand.class);
        register(PauseCommand.class);
        register(StopCommand.class);
        register(SkipCommand.class);
        register(ShuffleCommand.class);
        register(NowPlayingCommand.class);
        register(QueueCommand.class);
        register(RestartCommand.class);
        register(RepeatCommand.class);
        register(ResetCommand.class);
        register(VoteSkipCommand.class);

        //register(new JSCommandExecutor(new File("data/scripting/javascript/firstJSCommand.js")));
    }


    public void register(Class<? extends CommandExecutor> cls) {
        if (!cls.isAnnotationPresent(Command.class)) {
            throw new IllegalStateException("@Command annotation not found for class: " + cls.getName());
        }

        try {
            CommandExecutor cmd = cls.newInstance();

            Command annot = cls.getAnnotation(Command.class);

            cmd.setAliases(annot.aliases());
            cmd.setDescription(annot.description());
            cmd.setLevel(annot.level());
            cmd.setShownInHelp(annot.showInHelp());
            cmd.setUsage(annot.usage());

            for (Field field : cls.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    cmd.setInject(true);
                    break;
                }
            }

            for (String alias : cmd.getAliases()) {
                registerCommand(alias, cmd);
            }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void register(CommandExecutor jsc) {
        for (String alias : jsc.getAliases()) {
            registerCommand(alias, jsc);
        }
    }


    /**
     * Register the CommandExecutor instance into the registry.
     *  @param label Invoking key.
     * @param cmd   CommandExecutor instance.
     */
    public void registerCommand(String label, CommandExecutor cmd) {
        label = label.toLowerCase();
        if (commandMap.containsKey(label)) {
            throw new IllegalStateException("Command " + label + " is already registered.");
        }
        commandMap.put(label, cmd);
    }

    /**
     * Unregisters a CommandExecutor.
     *
     * @param label Invoking key.
     */
    public void unregisterCommand(String label) {
        commandMap.remove(label);
    }

    /**
     * Returns the command registry.
     *
     * @return The command registry.
     */
    public Map<String, CommandExecutor> getCommandMap() {
        return commandMap;
    }

    /**
     * @return Unique command executors.
     */
    public Set<CommandExecutor> getUniqueExecutors() {
        return new LinkedHashSet<>(commandMap.values());
    }

    /**
     * Get the command based on the key.
     *
     * @param key Invoking key.
     * @return CommandExecutor instance.
     */
    public CommandExecutor getCommand(String key) {
        return getCommandMap().get(key);
    }

    /**
     * Returns the CommandExecutor based on the class.
     *
     * @param cls CommandExecutor class.
     * @return CommandExecutor instance.
     */
    public CommandExecutor getCommand(Class<? extends CommandExecutor> cls) {
        Optional<CommandExecutor> cmd = commandMap.values()
                .stream()
                .filter(commandExecutor -> commandExecutor.getClass() == cls)
                .findFirst();

        return cmd.orElse(null);
    }
}
