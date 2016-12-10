package xyz.gnarbot.gnar.commands.polls;

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

/**
 * Created by zacha on 10/8/2016.
 */
@Command(aliases = "embedthis", usage = "(argument)")
public class TestEmbedCommand extends CommandExecutor {
	@Override
	public void execute(Note msg, String label, String[] args) {
		EmbedBuilder eb = new EmbedBuilder();
		String s = StringUtils.join(args, " ");
		Random r = new Random();
		eb.setTitle("**Message from " + msg.getAuthor().getName() + "**").addBlankField(true).setDescription(s).setThumbnail(msg.getAuthor().getAvatarUrl())
				.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
		MessageEmbed embed = eb.build();
		MessageBuilder mb = new MessageBuilder();
		mb.setEmbed(embed);
		Message m = mb.build();
		msg.getChannel().sendMessage(m).queue();
	}
}
