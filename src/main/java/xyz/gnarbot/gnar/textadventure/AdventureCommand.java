package xyz.gnarbot.gnar.textadventure;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

/**
 * Created by zacha on 10/8/2016.
 */
@Command(aliases = "adventure", usage = "(argument)", description = "Respond to Adventure things!")
public class AdventureCommand extends CommandExecutor {
	@Override
	public void execute(Note msg, String label, String[] args) {
		TextAdventure adventure = TextAdventure.getAdventure(msg.getAuthor(), msg);
		if (args.length > 0){
			if (args[0].equalsIgnoreCase("help")){
				String reply =
					"*Adventure Help!~*\n```XL\n"+
					" _adventure help - This list\n"+
					" _adventure actions - List of previous actions\n"+
					" _adventure quit - Ends the adventure.\n"+
					" _adventure {response} - Respond to a question, or do an action."+
						"\n```";
				msg.reply(reply);
				return;
			}
			adventure.parseResponse(msg, StringUtils.join(args, " "));
		}else{
			adventure.sendLastMessage(msg, "**No response given. Use `_adventure help` for a command list.**\nSending last message:");
		}


	}
}
