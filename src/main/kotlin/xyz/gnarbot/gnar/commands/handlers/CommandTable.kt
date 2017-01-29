package xyz.gnarbot.gnar.commands.handlers

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.commands.executors.`fun`.*
import xyz.gnarbot.gnar.commands.executors.admin.GarbageCollectCommand
import xyz.gnarbot.gnar.commands.executors.admin.JavascriptCommand
import xyz.gnarbot.gnar.commands.executors.admin.ShardInfoCommand
import xyz.gnarbot.gnar.commands.executors.admin.ThrowError
import xyz.gnarbot.gnar.commands.executors.games.GameLookupCommand
import xyz.gnarbot.gnar.commands.executors.games.LeagueLookupCommand
import xyz.gnarbot.gnar.commands.executors.games.OverwatchLookupCommand
import xyz.gnarbot.gnar.commands.executors.general.*
import xyz.gnarbot.gnar.commands.executors.media.*
import xyz.gnarbot.gnar.commands.executors.mod.*
import xyz.gnarbot.gnar.commands.executors.music.JoinCommand
import xyz.gnarbot.gnar.commands.executors.music.NowPlayingCommand
import xyz.gnarbot.gnar.commands.executors.music.PlayCommand
import xyz.gnarbot.gnar.commands.executors.music.QueueCommand
import xyz.gnarbot.gnar.commands.executors.music.dj.*
import xyz.gnarbot.gnar.commands.executors.polls.PollCommand
import xyz.gnarbot.gnar.commands.executors.test.TestCommand
import xyz.gnarbot.gnar.commands.executors.test.TestEmbedCommand
import xyz.gnarbot.gnar.textadventure.AdventureCommand
import xyz.gnarbot.gnar.textadventure.StartAdventureCommand
import kotlin.reflect.KClass
import kotlin.jvm.JvmStatic as static

/**
 * Holds and classifies commands.
 */
object CommandTable {
    val allAliases = mutableListOf<String>()

    val commands = mutableMapOf<String, CommandExecutor>()

    init {
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
        register(ReactCommand::class)
        register(ChampDataCommand::class)
        //register(DiscordBotsUserInfoCommand::class)
        register(TriviaAnswerCommand::class)
        register(TriviaCommand::class)
        register(GraphCommand::class)
        register(UrbanDictionaryCommand::class)
        register(ChampQuoteCommand::class)
        register(CleverBotCommand::class)
        register(PandoraBotCommand::class)
        register(MemeCommand::class)
        //End Fun Commands

        //Mod Commands
        register(BanCommand::class)
        register(KickCommand::class)
        register(UnbanCommand::class)
        register(PruneCommand::class)
        register(MuteCommand::class)
        register(DeleteMessageCommand::class)
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
         //register(GarfieldCommand::class)
        register(XKCDCommand::class)
        //End Media Commands

        // Administrator commands
        register(GarbageCollectCommand::class)
        register(JavascriptCommand::class)
        register(ShardInfoCommand::class)
        register(ThrowError::class)

        // Test Commands
        register(TestEmbedCommand::class)
        register(QuoteCommand::class)
        register(TextToBrickCommand::class)

        //MUSIC COMMAND
        register(JoinCommand::class)
        register(LeaveCommand::class)
        register(PlayCommand::class)
        register(PauseCommand::class)
        register(StopCommand::class)
        register(SkipCommand::class)
        register(NowPlayingCommand::class)
        register(QueueCommand::class)
        register(RestartCommand::class)
        register(RepeatCommand::class)
        register(ResetCommand::class)
    }

    /**
     * Check to see if [str] is a valid command invocation.
     */
    fun checkValid(str: String): Boolean {
        return allAliases.any { str.startsWith("${Bot.token}$it", true) }
    }

    /**
     * Register the command class.
     *
     * @param cls Class to register.
     */
    fun register(cls: Class<out CommandExecutor>) {
        if (!cls.isAnnotationPresent(Command::class.java)) {
            throw IllegalStateException("@Command annotation not found for class: ${cls.name}")
        }

        val cmd = cls.newInstance()

        val annot = cls.getAnnotation(Command::class.java)
        allAliases += annot.aliases

        cmd.setAliases(*annot.aliases)
        cmd.description = annot.description
        cmd.clearance = annot.clearance
        cmd.isShownInHelp = annot.showInHelp
        cmd.usage = annot.usage
        cmd.isCopy = annot.copy
        cmd.isInject = annot.inject

        for (alias in cmd.aliases) {
            commands[alias] = cmd
        }
    }

    /**
     * Register the command class.
     *
     * @param kCls Class to register.
     */
    fun register(kCls: KClass<out CommandExecutor>) = register(kCls.java)
}
