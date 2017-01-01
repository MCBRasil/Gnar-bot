package xyz.gnarbot.gnar.commands.general

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.handlers.members.Clearance
import xyz.gnarbot.gnar.utils.BotData
import xyz.gnarbot.gnar.utils.Note
import java.util.StringJoiner

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
                    "Description __ ${cmd.description}",
                    "Usage ________ ${Bot.token}${args[0].toLowerCase()} ${cmd.usage}",
                    "Aliases ______ [${aliases.joinToString(", ")}]"
            )
            
            message.replyEmbedRaw("", strings.joinToString("\n"), Bot.color)
            
            return
        }
        
        val commandEntries = host.commandHandler.uniqueRegistry
        
        val eb = EmbedBuilder()
        eb.setTitle("GNAR Help")
        eb.setDescription("This is all of GN4R-Bot's currently registered commands on the __**${host.name}**__ guild.\n\n")
        eb.setColor(Bot.color)
        
        for (perm in Clearance.values())
        {
            val count = commandEntries.values.filter { it.clearance == perm && it.isShownInHelp }.count()
            if (count < 1) continue
            
            val lineBuilder = StringBuilder()
            for (i in 0 .. 22 - perm.toString().length) lineBuilder.append('—')
            
            if (count < 10)
            {
                val builder = StringJoiner("\n")
                builder.add("__**${perm.toString().replace("_", " ")}**  $count __\n")
                for ((cmdLabel, cmd) in commandEntries)
                {
                    if (cmd.clearance != perm || !cmd.isShownInHelp) continue
                    
                    builder.add("**[${Bot.token}$cmdLabel]()** ${cmd.usage}")
                }
                
                eb.addField("", builder.toString(), false)
            }
            else
            {
                eb.addField("", "__**${perm.toString().replace("_", " ")}**  $count __", false)
                var builder = StringJoiner("\n")
                var cmdID = 0
                var embedCount = 0
                for ((cmdLabel, cmd) in commandEntries)
                {
                    if (cmd.clearance != perm || !cmd.isShownInHelp) continue
                    cmdID++
                    builder.add("**[${Bot.token}$cmdLabel]()**")
                    if (cmdID == 14)
                    {
                        eb.addField("", builder.toString(), true)
                        embedCount++
                        builder = StringJoiner("\n")
                        cmdID = 0
                    }
                }
            }
        }
        
        val builder = StringBuilder()
        
        builder.append("To view a command's description, do `${Bot.token}help [command]`.\n\n")
        //builder.append("You can also chat and execute commands with Gnar privately, try it!\n\n")
        
        builder.append("**Bot Commander** commands requires a role named exactly __[Bot Commander]()__.\n")
        builder.append("**Server Owner** commands requires you to be the __[server owner]()__ to execute.\n")
        
        builder.append("\n")
        
        builder.append("**Latest News:**\n")
        builder.append(" - Gnar v2.0 is now running LIVE. Report any bugs you might find.\n")
        builder.append(" - Dropping a new look/style to Gnar's responses! Check them out!\n")
        builder.append(" - Text Adventures are fixed! Use `_startadventure`!\n")
        
        builder.append("\n")
        
        builder.append("**[Website](http://gnarbot.xyz)**\n")
        builder.append("**[Discord Server](http://discord.gg/NQRpmr2)** \n")
        
        eb.addField("", builder.toString(), false)
        
        //message.reply(builder.toString())
        
        if (!message.author?.hasPrivateChannel()!!)
        {
            message.author?.openPrivateChannel()?.complete()
        }
        val embed = eb.build()
        val mb = MessageBuilder()
        mb.setEmbed(embed)
        val m = mb.build()
        message.author?.privateChannel?.sendMessage(m)?.complete(true)
        
        message.reply("**${BotData.randomQuote()}** My commands has been PM'ed to you.")
    }
}