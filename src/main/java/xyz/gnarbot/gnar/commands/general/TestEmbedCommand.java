package xyz.gnarbot.gnar.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.awt.*;
import java.util.Random;

@Command(aliases = "embedthis", usage = "(argument)")
public class TestEmbedCommand extends CommandExecutor
{
    @Override
    public void execute(Note msg, String label, String[] args)
    {
        EmbedBuilder eb = new EmbedBuilder();
        String s = StringUtils.join(args, " ");
        Random r = new Random();
        String[] parts = s.split(":newsection:");
        eb.setTitle("**Message from " + msg.getAuthor().getName() + "**");
        eb.setDescription(parts[0]);
        parts[0] = "";
        if (parts.length > 1){
            int id = 0;
            for (String p : parts){
                if (p != "") {
                    id++;
                    eb.addField("Section " + id, p, false);
                }
            }
        }
        eb.setThumbnail(msg.getAuthor().getAvatarUrl()).setColor(new Color(r.nextInt(255), r.nextInt(255), r
                    .nextInt(255)));
        MessageEmbed embed = eb.build();
        MessageBuilder mb = new MessageBuilder();
        mb.setEmbed(embed);
        Message m = mb.build();
        msg.getChannel().sendMessage(m).queue();
    }
}
