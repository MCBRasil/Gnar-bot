package xyz.gnarbot.gnar.commands.polls;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.textadventure.TextAdventure;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

/**
 * Created by zacha on 10/8/2016.
 */
@Command(aliases = "poll", usage = "(argument)", description = "Do poll-y stuff!")
public class PollCommand extends CommandExecutor {
	@Override
	public void execute(Note msg, String label, String[] args) {
		if (args.length == 0 || (args.length > 0 && args[0].equalsIgnoreCase("help"))){
			String reply =
					"*Poll System Help!~*\n {} = Required Arguments  |  () = Optional Arguments```Markdown\n" +
							"[_poll help](This list.)\n" +
							"[_poll startyesno {time} {question}](Start a Yes/No Poll for \"time\" minutes.)" +
							"\n```";
			msg.reply(reply);
			return;
		}
		else if (args.length > 0){
			if (args[0].equalsIgnoreCase("startyesno") && args.length > 1){
				int time = 15;
				try{
					time = Integer.parseInt(args[1].trim());
				}catch (NumberFormatException e){
				}
				args[0] = "";
				args[1] = "";
				String q = "";
				for (String s : args){
					if (!s.equalsIgnoreCase("")){
						q += s + " ";
					}
				}
				q = q.trim();
				PollManager.registerPoll(new YesNoPoll(msg, q, time));
			}
		}
	}
}
