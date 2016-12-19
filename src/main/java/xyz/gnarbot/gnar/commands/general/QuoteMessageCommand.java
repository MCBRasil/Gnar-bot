package xyz.gnarbot.gnar.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.BotData;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Command(
        aliases = "quotemsg",
        usage = "(messageid)",
        description = "Quote somebody else.."
)
public class QuoteMessageCommand extends CommandExecutor
{
    @Override
    public void execute(Note message, String label, String[] args)
    {
        if (args.length < 1)
        {
            message.reply("**" + BotData.randomQuote() + "** Well that sounds fantastic! I'll just quote th-... You didn't give me anything to work with. *(I need the message ID)*");
            return;
        }
        if (args.length == 1) {
            String msgid = args[0];
            final Note msg = message.reply("**" + BotData.randomQuote() + "** Searching for Message with ID `" + msgid + "`.");
            try {
                Message m = msg.getChannel().getMessageById(msgid).block();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("**Quote from " + m.getAuthor().getName() + "**").addBlankField(true).setDescription(m.getContent()).setThumbnail(m.getAuthor().getAvatarUrl())
                        .setColor(new Color(0xFFDD15));
                MessageEmbed embed = eb.build();
                MessageBuilder mb = new MessageBuilder();
                mb.setEmbed(embed);
                Message embedMsg = mb.build();
                message.getChannel().sendMessage(embedMsg).queue();
                msg.deleteMessage().queue();
            } catch (Exception e) {
                msg.editMessage("**" + BotData.randomQuote() + "** Oops. I couldn't find that message within this channel. You sure you got the right place?").queue();
                Bot.INSTANCE.getScheduler().schedule(new Runnable() {
                    @Override
                    public void run() {
                        msg.deleteMessage().queue();
                    }
                }, 10, TimeUnit.SECONDS);
            }
        }else{
            String combined = StringUtils.join(args, ", ");
            final Note msg = message.reply("**" + BotData.randomQuote() + "** Searching for Messages with IDs `" + combined + "`.");
            EmbedBuilder eb = new EmbedBuilder();
            int count = 0;
            for (String msgid : args) {
                try {
                    count++;
                    Message m = msg.getChannel().getMessageById(msgid).block();
                    if (count == 1) {
                        eb.setTitle("**Quote from " + m.getAuthor().getName() + " (" + m.getCreationTime().format(DateTimeFormatter.ISO_DATE_TIME) + ")**").addBlankField(true).setDescription(m.getContent()).setThumbnail(m.getAuthor().getAvatarUrl())
                                .setColor(new Color(0xFFDD15));
                    }else{
                        eb.addBlankField(true);
                        eb.addField("**Quote from " + m.getAuthor().getName() + " (" + m.getCreationTime().format(DateTimeFormatter.ISO_DATE_TIME) + ")**", m.getContent(), false);
                    }
                } catch (Exception e) {
                    if (count == 1) {
                        eb.setTitle("*Couldn't find this message*").addBlankField(true).setDescription("*Couldn't find message with ID " + msgid + "*")
                                .setColor(Color.RED);
                    }else{
                        eb.addBlankField(true);
                        eb.addField("*Couldn't find this message*", "*Couldn't find message with ID " + msgid + "*", false);
                        eb.setColor(Color.RED);
                    }
                }
            }
            MessageEmbed embed = eb.build();
            MessageBuilder mb = new MessageBuilder();
            mb.setEmbed(embed);
            Message embedMsg = mb.build();
            message.getChannel().sendMessage(embedMsg).queue();
            msg.deleteMessage().queue();
        }
    }
}



