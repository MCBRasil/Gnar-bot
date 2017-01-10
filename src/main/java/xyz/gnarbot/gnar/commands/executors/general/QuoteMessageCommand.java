package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.BotData;
import xyz.gnarbot.gnar.utils.Note;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

//TODO REMOVE ALL THE EDIT STUFF
@Command(aliases = "quotemsg", usage = "(messageid)", description = "Quote somebody else..")
public class QuoteMessageCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (args.length < 1)
        {
            note.error("**" + BotData.randomQuote() + "** Well that sounds fantastic! I'll just quote th-... You " +
                    "didn't give me anything to work with. *(I need the message ID)*");
            return;
        }
        if (args.length == 1)
        {
            String msgid = args[0];
            final Note msg = note.reply("**" + BotData.randomQuote() + "** Searching for Message with ID `" + msgid +
                    "`.");
            try
            {
                Message m = msg.getChannel().getMessageById(msgid).complete();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("**Quote from " + m.getAuthor().getName() + "**")
                        .addBlankField(true)
                        .setDescription(m.getContent())
                        .setThumbnail(m.getAuthor().getAvatarUrl())
                        .setColor(new Color(0xFFDD15));
                MessageEmbed embed = eb.build();
                MessageBuilder mb = new MessageBuilder();
                mb.setEmbed(embed);
                Message embedMsg = mb.build();
                note.getChannel().sendMessage(embedMsg).queue();
                msg.deleteMessage().queue();
            }
            catch (Exception e)
            {
                msg.editMessage("**" + BotData.randomQuote() + "** Oops. I couldn't find that message within this " +
                        "channel. You sure you got the right place?")
                        .queue();
                Bot.INSTANCE.getScheduler().schedule(() -> msg.deleteMessage().queue(), 10, TimeUnit.SECONDS);
            }
        }
        else
        {
            String combined = StringUtils.join(args, ", ");
            final Note msg = note.reply("**" + BotData.randomQuote() + "** Searching for Messages with IDs `" +
                    combined + "`.");
            EmbedBuilder eb = new EmbedBuilder();
            int count = 0;
            for (String msgid : args)
            {
                try
                {
                    count++;
                    Message m = msg.getChannel().getMessageById(msgid).complete();
                    if (count == 1)
                    {
                        eb.setTitle("**Quote from " + m.getAuthor().getName() + " (" + m.getCreationTime()
                                .format(DateTimeFormatter.ISO_DATE_TIME) + ")**")
                                .addBlankField(true)
                                .setDescription(m.getContent())
                                .setThumbnail(m.getAuthor().getAvatarUrl())
                                .setColor(new Color(0xFFDD15));
                    }
                    else
                    {
                        eb.addBlankField(true);
                        eb.addField("**Quote from " + m.getAuthor().getName() + " (" + m.getCreationTime()
                                .format(DateTimeFormatter.ISO_DATE_TIME) + ")**", m.getContent(), false);
                    }
                }
                catch (Exception e)
                {
                    if (count == 1)
                    {
                        eb.setTitle("*Couldn't find this message*")
                                .addBlankField(true)
                                .setDescription("*Couldn't " + "find message with ID " + msgid + "*")
                                .setColor(Bot.getColor());
                    }
                    else
                    {
                        eb.addBlankField(true);
                        eb.addField("*Couldn't find this message*", "*Couldn't find message with ID " + msgid + "*",
                                false);
                        eb.setColor(Bot.getColor());
                    }
                }
            }
            MessageEmbed embed = eb.build();
            MessageBuilder mb = new MessageBuilder();
            mb.setEmbed(embed);
            Message embedMsg = mb.build();
            note.getChannel().sendMessage(embedMsg).queue();
            msg.deleteMessage().queue();
        }
    }
}



