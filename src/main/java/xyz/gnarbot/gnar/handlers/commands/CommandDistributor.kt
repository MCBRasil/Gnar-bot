package xyz.gnarbot.gnar.handlers.commands

import com.google.inject.Inject
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.`fun`.*
import xyz.gnarbot.gnar.commands.admin.*
import xyz.gnarbot.gnar.commands.games.*
import xyz.gnarbot.gnar.commands.general.*
import xyz.gnarbot.gnar.commands.media.*
import xyz.gnarbot.gnar.commands.mod.*
import xyz.gnarbot.gnar.commands.polls.PollCommand
import xyz.gnarbot.gnar.commands.test.TestCommand
import xyz.gnarbot.gnar.textadventure.*
import java.lang.reflect.Field
import kotlin.reflect.KClass
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
        register(HelpCommand::class)
        register(InviteBotCommand::class)
        register(PingCommand::class)
        register(MathCommand::class)
        register(RemindMeCommand::class)
        register(GoogleCommand::class)
        register(YoutubeCommand::class)
        register(UptimeCommand::class)
        register(WhoIsCommand::class)
        register(BotInfoCommand::class)
        //End General Commands
    
        //Fun Commands
        register(ASCIICommand::class)
        register(CoinFlipCommand::class)
        register(DialogCommand::class)
        register(YodaTalkCommand::class)
        register(RollCommand::class)
        register(PoopCommand::class)
        register(GoodShitCommand::class)
        register(EightBallCommand::class)
        register(LeetifyCommand::class)
        register(MarvelComics::class)
        register(ProgressionCommand::class)
        register(Rule34Command::class)
        register(ServersSharedCommand::class)
        register(TextToSpeechCommand::class)
        register(ReactToMessageCommand::class)
        register(ChampDataCommand::class)
        register(DiscordBotsUserInfoCommand::class)
        register(TriviaAnswerCommand::class)
        register(TriviaCommand::class)
        register(GraphCommand::class)
        register(UrbanDictionaryCommand::class)
        register(ChampQuoteCommand::class)
        register(CleverBotCommand::class)
        register(PandoraBotCommand::class)
        //End Fun Commands
    
        //Mod Commands
        register(BanCommand::class)
        register(KickCommand::class)
        register(UnbanCommand::class)
        register(DeleteMessagesCommand::class)
        //End Mod Commands
    
        //Testing Commands
        register(TestCommand::class)
        //End Testing Commands
    
        //Text Adventure Commands
        register(AdventureCommand::class)
        register(StartAdventureCommand::class)
        //End Text Adventure Commands
    
        //Game Commands
        register(OverwatchLookupCommand::class)
        register(LeagueLookupCommand::class)
        register(GameLookupCommand::class)
        //End Game Commands
    
        //Poll Commands
        register(PollCommand::class)
        //End Poll Commands
    
        //Media Commands
        register(CatsCommand::class)
        register(ExplosmCommand::class)
        register(ExplosmRCGCommand::class)
        register(GarfieldCommand::class)
        register(XKCDCommand::class)
        //End Media Commands
    
        // Administrator commands
        register(JavascriptCommand::class)
        register(ShardInfoCommand::class)
        register(ThrowError::class)
        
        // Test Commands
        register(TestEmbedCommand::class)
        register(QuoteMessageCommand::class)
        register(TextToBrickCommand::class)
        register(DeleteMessageCommand::class)
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
    
    fun register(kCls : KClass<out CommandExecutor>) = register(kCls.java)
    
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
