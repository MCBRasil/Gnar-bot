package xyz.gnarbot.gnar.textadventure;


import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.io.IOException;
import java.util.List;


/**
 * Created by zacha on 10/8/2016.
 */
@Command(aliases = "adventure", usage = "(argument)", description = "Respond to Adventure things!")
public class AdventureCommand extends CommandExecutor {
	@Override
	public void execute(Note msg, String label, String[] args) {
		TextAdventure adventure = TextAdventure.getAdventure(msg.getAuthor(), msg);
		if (args.length > 0){
			if (args[0].equalsIgnoreCase("map")){
				msg.reply("Preparing your map...");
				adventure.getGrid().sendMap(msg);
				return;
			}
			if (args[0].equalsIgnoreCase("inventory")){
				if (adventure.getInventory() != null) {
					msg.reply(adventure.getInventory().toString());
					return;
				}
			}
			if (args[0].equalsIgnoreCase("setname")){
				if (args.length > 1){
					String oldname = adventure.getHeroName();
					adventure.setHeroName(StringUtils.join(args, " ").replace("setname ", ""));
					adventure.sendInformativeMessage(msg, ":bulb: The hero wishes to change their name? Very well then. I shall now refer to you as **" + adventure.getHeroName() + "**");
					adventure.logAction("Changed Hero name to " + adventure.getHeroName() + " from " + oldname);
					return;
				}else{
					adventure.sendInformativeMessage(msg, ":bulb: Hmm... I can't do that unless you tell me what to call you, Hero.");
					return;
				}
			}
			if (args[0].equalsIgnoreCase("actions")){
				if (adventure.getActionList().size() == 0){
					adventure.sendInformativeMessage(msg, "I couldn't find anything... Sorry :no_mouth:");
					return;
				}
				String r = "```Markdown\n";
				int count = 10;
				int done = 0;
				if (adventure.getActionList().size() < 10){
					count = adventure.getActionList().size();
				}
				List<String> list = (adventure.getActionList().size() > 10) ? adventure.getActionList().subList(adventure.getActionList().size() - 10, adventure.getActionList().size()) :
						adventure.getActionList();
				for (String s : list){
					if (done < count) {
						r += "[#" + done + "]" + s + "\n";
						done++;
					}else{
						r += "```";
						adventure.sendInformativeMessage(msg, r);
						return;
					}
				}
				r += "```";
				adventure.sendInformativeMessage(msg, r);
				return;
			}
			if (args[0].equalsIgnoreCase("last")){
				adventure.sendLastMessage(msg, "Sending last message as per your request~");
				return;
			}
			if (args[0].equalsIgnoreCase("help")){
				if (adventure.getInventory() == null) {
					String reply =
							"*Adventure Help!~*\n```ini\n" +
									"[_adventure help] This list.\n" +
									"[_adventure actions] List of previous actions.\n" +
									"[_adventure quit] Ends the adventure.\n" +
									"[_adventure setname {name}] Changes your name.\n" +
									"[_adventure {response}] Respond to a question, or do an action. \n" +
									"[_adventure last] Sends the last sent message. "+
									"\n```";
					msg.reply(reply);
				}else{
					String reply =
							"*Adventure Help!~*\n```ini\n" +
									"[_adventure help] This list.\n" +
									"[_adventure actions] List of previous actions.\n" +
									"[_adventure quit] Ends the adventure.\n" +
									"[_adventure setname {name}] Changes your name.\n" +
									"[_adventure {response}] Respond to a question, or do an action.\n" +
									"[_adventure last] Sends the last sent message.\n"+
									"[_adventure inventory] Displays your inventory."+
									"\n```";
					msg.reply(reply);

				}
				return;
			}
			adventure.parseResponse(msg, StringUtils.join(args, " "), false);
		}else{
			adventure.sendLastMessage(msg, "**No response given. Use `_adventure help` for a command list.**\nSending last message:");
		}


	}
}
