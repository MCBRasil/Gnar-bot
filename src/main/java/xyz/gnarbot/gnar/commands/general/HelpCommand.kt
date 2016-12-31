package xyz.gnarbot.gnar.commands.general

import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.handlers.members.Clearance
import xyz.gnarbot.gnar.utils.BotData
import xyz.gnarbot.gnar.utils.Note
import java.util.*

@Command(aliases = arrayOf("help", "guide"), usage = "~command", description = "Display GN4R's list of commands.")
class HelpCommand : CommandExecutor()
{
    override fun execute(message : Note, label : String, args : Array<out String>)
    {
        val host = message.host
        
        if (args.isNotEmpty())
        {
            val cmd : CommandExecutor? = host.commandHandler.getCommand(args[0])
            
            if (cmd == null)
            {
                message.replyRaw("There is no command by the name `${args[0]}` in this guild. :cry:")
                return
            }
            
            val aliases = host.commandHandler.registry.entries
                    .filter { it.value == cmd }
                    .map { it.key }
            
            val strings = listOf(
                    "```",
                    "\u258C Description __ ${cmd.description}",
                    "\u258C Usage ________ ${Bot.token}${args[0].toLowerCase()} ${cmd.usage}",
                    "\u258C Aliases ______ [${aliases.joinToString(", ")}]",
                    "```"
            )
            
            message.replyRaw(strings.joinToString("\n"))
            
            return
        }
        
        val commandEntries = host.commandHandler.uniqueRegistry
        
        val builder = StringBuilder()
        
        builder.append("\nThis is all of GN4R-Bot's currently registered commands on the __**${host.name}**__ guild.\n\n")
        
        for (perm in Clearance.values())
        {
            val count = commandEntries.values.filter { it.clearance == perm && it.isShownInHelp }.count()
            if (count < 1) continue
            
            val joiner = StringJoiner("", "```ini\n", "```\n")
            
            val lineBuilder = StringBuilder()
            for (i in 0 .. 22 - perm.toString().length) lineBuilder.append('—')
            
            joiner.add("\u258c ${perm.toString().replace("_", " ")} $lineBuilder $count\n")
            
            for ((cmdLabel, cmd) in commandEntries)
            {
                if (cmd.clearance != perm || !cmd.isShownInHelp) continue
                
                joiner.add("\u258C  [${Bot.token}$cmdLabel] ${cmd.usage}\n")
            }
            
            builder.append(joiner.toString())
        }
        
        builder.append("To view a command's description, do `${Bot.token}help [command]`.\n\n")
        //builder.append("You can also chat and execute commands with Gnar privately, try it!\n\n")
        
        builder.append("**Bot Commander** commands requires you to have a role named exactly __Bot Commander__.\n")
        builder.append("**Server Owner** commands requires you to be the __Server Owner__ to execute.\n\n")
        
        builder.append("**Latest News:**\n")
        builder.append(" - Gnar v2.0 is now running LIVE. Report any bugs you might find.\n")
        builder.append(" - Text Adventures are fixed! Use `_startadventure`!\n")
        
        builder.append("**Website:** http://gnarbot.xyz\n")
        builder.append("**Discord Server:** http://discord.gg/NQRpmr2\n")
        
        //message.reply(builder.toString())

        message.author?.openPrivateChannel()?.queue({chan -> chan.sendMessage(builder.toString()).queue()})

        message.reply("**${BotData.randomQuote()}** My commands has been PM'ed to you.")
    }
}