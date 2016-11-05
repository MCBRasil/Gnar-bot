package xyz.gnarbot.gnar.commands.polls;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import xyz.gnarbot.gnar.handlers.members.Member;
import xyz.gnarbot.gnar.utils.Note;

import java.util.function.Consumer;

/**
 * Created by zacha on 11/5/2016.
 */
public class YesNoPoll {

	private Member startingUser;
	private long timeStarted;

	public YesNoPoll(Note n, String question, int minutes){
		System.out.println(n.getAuthor().getUsername() + " created a new poll");
		startingUser = n.getAuthor();
		Message m = n.getJDA().getTextChannelById(n.getChannelId()).getMessageById(n.getId());
	}

}
