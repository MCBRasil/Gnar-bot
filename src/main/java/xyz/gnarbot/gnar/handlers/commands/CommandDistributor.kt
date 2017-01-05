package xyz.gnarbot.gnar.handlers.commands

import com.google.inject.Inject
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.TestCommand
import xyz.gnarbot.gnar.commands.`fun`.ASCIICommand
import xyz.gnarbot.gnar.commands.`fun`.ChampDataCommand
import xyz.gnarbot.gnar.commands.`fun`.ChampQuoteCommand
import xyz.gnarbot.gnar.commands.`fun`.CleverBotCommand
import xyz.gnarbot.gnar.commands.`fun`.CoinFlipCommand
import xyz.gnarbot.gnar.commands.`fun`.DialogCommand
import xyz.gnarbot.gnar.commands.`fun`.DiscordBotsUserInfoCommand
import xyz.gnarbot.gnar.commands.`fun`.EightBallCommand
import xyz.gnarbot.gnar.commands.`fun`.GoodShitCommand
import xyz.gnarbot.gnar.commands.`fun`.GraphCommand
import xyz.gnarbot.gnar.commands.`fun`.LeetifyCommand
import xyz.gnarbot.gnar.commands.`fun`.MarvelComics
import xyz.gnarbot.gnar.commands.`fun`.PandoraBotCommand
import xyz.gnarbot.gnar.commands.`fun`.PoopCommand
import xyz.gnarbot.gnar.commands.`fun`.ProgressionCommand
import xyz.gnarbot.gnar.commands.`fun`.RollCommand
import xyz.gnarbot.gnar.commands.`fun`.Rule34Command
import xyz.gnarbot.gnar.commands.`fun`.ServersSharedCommand
import xyz.gnarbot.gnar.commands.`fun`.TextToBrickCommand
import xyz.gnarbot.gnar.commands.`fun`.TextToSpeechCommand
import xyz.gnarbot.gnar.commands.`fun`.TriviaAnswerCommand
import xyz.gnarbot.gnar.commands.`fun`.TriviaCommand
import xyz.gnarbot.gnar.commands.`fun`.UrbanDictionaryCommand
import xyz.gnarbot.gnar.commands.`fun`.YodaTalkCommand
import xyz.gnarbot.gnar.commands.games.GameLookupCommand
import xyz.gnarbot.gnar.commands.games.LeagueLookupCommand
import xyz.gnarbot.gnar.commands.games.OverwatchLookupCommand
import xyz.gnarbot.gnar.commands.general.BotInfoCommand
import xyz.gnarbot.gnar.commands.general.DeleteMessageCommand
import xyz.gnarbot.gnar.commands.general.GoogleCommand
import xyz.gnarbot.gnar.commands.general.HelpCommand
import xyz.gnarbot.gnar.commands.general.InviteBotCommand
import xyz.gnarbot.gnar.commands.general.MathCommand
import xyz.gnarbot.gnar.commands.general.PingCommand
import xyz.gnarbot.gnar.commands.general.QuoteMessageCommand
import xyz.gnarbot.gnar.commands.general.ReactToMessageCommand
import xyz.gnarbot.gnar.commands.general.RemindMeCommand
import xyz.gnarbot.gnar.commands.general.TestEmbedCommand
import xyz.gnarbot.gnar.commands.general.UptimeCommand
import xyz.gnarbot.gnar.commands.general.WhoIsCommand
import xyz.gnarbot.gnar.commands.general.YoutubeCommand
import xyz.gnarbot.gnar.commands.media.CatsCommand
import xyz.gnarbot.gnar.commands.media.ExplosmCommand
import xyz.gnarbot.gnar.commands.media.ExplosmRCGCommand
import xyz.gnarbot.gnar.commands.media.GarfieldCommand
import xyz.gnarbot.gnar.commands.media.XKCDCommand
import xyz.gnarbot.gnar.commands.mod.BanCommand
import xyz.gnarbot.gnar.commands.mod.DeleteMessagesCommand
import xyz.gnarbot.gnar.commands.mod.KickCommand
import xyz.gnarbot.gnar.commands.mod.UnbanCommand
import xyz.gnarbot.gnar.commands.polls.PollCommand
import xyz.gnarbot.gnar.textadventure.AdventureCommand
import xyz.gnarbot.gnar.textadventure.StartAdventureCommand
import java.lang.reflect.Field
import kotlin.jvm.JvmStatic as static

/**
 * Holds and classifies commands.
 */
object CommandDistributor
{
    val allAliases = mutableListOf<String>()
    
    val singletonCommands = mutableMapOf<String, CommandExecutor>()
    val managedCommands = mutableListOf<Class<out CommandExecutor>>()
    
    init
    {
        //General Commands
        register(HelpCommand::class.java)
        register(InviteBotCommand::class.java)
        register(PingCommand::class.java)
        register(MathCommand::class.java)
        register(RemindMeCommand::class.java)
        register(GoogleCommand::class.java)
        register(YoutubeCommand::class.java)
        register(UptimeCommand::class.java)
        register(WhoIsCommand::class.java)
        register(BotInfoCommand::class.java)
        //End General Commands
    
        //Fun Commands
        register(ASCIICommand::class.java)
        register(CoinFlipCommand::class.java)
        register(DialogCommand::class.java)
        register(YodaTalkCommand::class.java)
        register(RollCommand::class.java)
        register(PoopCommand::class.java)
        register(GoodShitCommand::class.java)
        register(EightBallCommand::class.java)
        register(LeetifyCommand::class.java)
        register(MarvelComics::class.java)
        register(ProgressionCommand::class.java)
        register(Rule34Command::class.java)
        register(ServersSharedCommand::class.java)
        register(TextToSpeechCommand::class.java)
        register(ReactToMessageCommand::class.java)
        register(ChampDataCommand::class.java)
        register(DiscordBotsUserInfoCommand::class.java)
        register(TriviaAnswerCommand::class.java)
        register(TriviaCommand::class.java)
        register(GraphCommand::class.java)
        register(UrbanDictionaryCommand::class.java)
        register(ChampQuoteCommand::class.java)
        register(CleverBotCommand::class.java)
        register(PandoraBotCommand::class.java)
        //End Fun Commands
    
        //Mod Commands
        register(BanCommand::class.java)
        register(KickCommand::class.java)
        register(UnbanCommand::class.java)
        register(DeleteMessagesCommand::class.java)
        //End Mod Commands
    
        //Testing Commands
        register(TestCommand::class.java)
        //End Testing Commands
    
        //Text Adventure Commands
        register(AdventureCommand::class.java)
        register(StartAdventureCommand::class.java)
        //End Text Adventure Commands
    
        //Game Commands
        register(OverwatchLookupCommand::class.java)
        register(LeagueLookupCommand::class.java)
        register(GameLookupCommand::class.java)
        //End Game Commands
    
        //Poll Commands
        register(PollCommand::class.java)
        //End Poll Commands
    
        //Media Commands
        register(CatsCommand::class.java)
        register(ExplosmCommand::class.java)
        register(ExplosmRCGCommand::class.java)
        register(GarfieldCommand::class.java)
        register(XKCDCommand::class.java)
        //End Media Commands
    
        // Test Commands
        register(TestEmbedCommand::class.java)
        register(QuoteMessageCommand::class.java)
        register(TextToBrickCommand::class.java)
        register(DeleteMessageCommand::class.java)
    }
    
    fun checkValid(str : String) : Boolean
    {
        return allAliases.any { str.startsWith("${Bot.token}$it") }
    }
    
    /** Command type classifications. */
    enum class CommandType
    {
        /** Does not inject anything. Same instances across all hosts. */
        SINGLETON,
        /** Need to inject something. Different instances. */
        MANAGED
    }
    
    // DEPENDENCY: REFLECTIONS
//    @Suppress("UNCHECKED_CAST")
//    fun registerAll(packages : String)
//    {
//        val reflections = Reflections(packages)
//
//        reflections.getTypesAnnotatedWith(Command::class.java)
//                .forEach { register(it as Class<out CommandExecutor>) }
//    }
    
    /**
     * Register the command class and automatically
     * classify the command.
     *
     * @param cls Class to register.
     */
    fun register(cls : Class<out CommandExecutor>)
    {
        if (!cls.isAnnotationPresent(Command::class.java))
        {
            throw IllegalStateException("@Command annotation not found for class: ${cls.name}")
        }
        
        when (classify(cls))
        {
            CommandType.SINGLETON -> registerAsSingleton(cls)
            CommandType.MANAGED -> registerAsManaged(cls)
        }
    }
    
    /**
     * Register command as a singleton command.
     *
     * @param cls Class to register.
     */
    fun registerAsSingleton(cls : Class<out CommandExecutor>)
    {
        val cmd = cls.newInstance()
        
        val meta = cls.getAnnotation(Command::class.java)
        allAliases += meta.aliases
        
        cmd.setAliases(*meta.aliases)
        cmd.description = meta.description
        cmd.clearance = meta.clearance
        cmd.isShownInHelp = meta.showInHelp
        cmd.usage = meta.usage
    
        
        for (alias in cmd.aliases)
        {
            singletonCommands[alias] = cmd
        }
    }
    
    /**
     * Register command as a managed command.
     *
     * @param cls Class to register.
     */
    fun registerAsManaged(cls : Class<out CommandExecutor>)
    {
        val meta = cls.getAnnotation(Command::class.java)
        allAliases += meta.aliases
        
        managedCommands += cls
    }
    
    /**
     * Find all field within a class that is annotated with @Inject.
     *
     * @see Inject
     * @param cls Target class.
     * @return Fields in target class annotated with [@Inject][Inject].
     */
    @static fun findInjectableFields(cls : Class<*>) : Array<Field>
    {
        val fields = cls.declaredFields.filter { it.isAnnotationPresent(Inject::class.java) }
        
        return fields.toTypedArray()
    }
    
    /**
     * Classify the command based on it's injectable fields.
     *
     * @param cls Target class.
     * @return Command type.
     */
    @static fun classify(cls : Class<out CommandExecutor>) : CommandType
    {
        val types = findInjectableFields(cls).map { it.type }
        
        return when
        {
            types.isNotEmpty() -> CommandType.MANAGED
            else -> CommandType.SINGLETON
        }
    }
}
