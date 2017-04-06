package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;

@Command(aliases = {"adventure", "adv", "pokemon"}, usage = "(argument)", description = "Respond to Adventure things!")
public class AdventureCommand extends CommandExecutor {
    @SuppressWarnings("all")
    @Override
    public void execute(Message message, List<String> args) {
        if (!Adventure.hasAdventure(message.getAuthor())) {
            message.respond().info("Use `_startadventure` to begin your adventure!").queue();
            return;
        }
        Adventure adventure = Adventure.getAdventure(message.getAuthor(), message, getBot());

        if (args.size() > 0) {
            switch (args.get(0).toLowerCase()) {
                case "quit": {
                    message.respond().info("Poof! Your adventure has been lost to the winds of Azaroth.").queue();
                    Adventure.removeAdventure(message.getAuthor());
                    return;
                }
                case "map": {
                    message.respond().info("Preparing your map...").queue();
                    adventure.getGrid().sendMap(message);
                    return;
                }
                case "inventory": {
                    if (adventure.getInventory() != null) {
                        adventure.sendInformativeMessage(message, adventure.getInventory().toString());
                        return;
                    }
                }
                case "setname": {
                    if (args.size() > 1) {
                        String oldname = adventure.getHeroName();
                        adventure.setHeroName(StringUtils.join(args, " ").replace("setname ", ""));
                        adventure.sendInformativeMessage(message,
                                ":bulb: The hero wishes to change their name? Very well " +
                                        "then . I shall now refer to you as **" + adventure
                                        .getHeroName() + "**");
                        adventure.logAction("Changed Hero name to " + adventure.getHeroName() + " from " + oldname);
                        return;
                    } else {
                        adventure.sendInformativeMessage(message,
                                ":bulb: Hmm... I can't do that unless you tell me what to " +
                                        "call you, Hero.");
                        return;
                    }
                }
                case "actions": {
                    if (adventure.getActionList().size() == 0) {
                        adventure.sendInformativeMessage(message, "I couldn't find anything... Sorry :no_mouth:");
                        return;
                    }
                    String r = "```Markdown\n";
                    int count = 10;
                    int done = 0;
                    if (adventure.getActionList().size() < 10) {
                        count = adventure.getActionList().size();
                    }
                    List<String> list = (adventure.getActionList().size() > 10) ? adventure.getActionList()
                            .subList(adventure.getActionList().size() - 10, adventure.getActionList()
                                    .size()) : adventure.getActionList();
                    for (String s : list) {
                        if (done < count) {
                            r += "[#" + done + "]" + s + "\n";
                            done++;
                        } else {
                            r += "```";
                            adventure.sendInformativeMessage(message, r);
                            return;
                        }
                    }
                    r += "```";
                    adventure.sendInformativeMessage(message, r);
                    return;
                }
                case "last": {
                    adventure.sendLastMessage(message, "Sending last message as per your request~");
                    return;
                }
                case "help": {
                    if (adventure.getInventory() == null) {
                        String reply = "*Adventure Help!~*\n```ini\n" + "[_adventure help] This list.\n" + "[_adventure " +
                                "actions] List of previous actions.\n" + "[_adventure quit] Ends the adventure.\n" +
                                "[_adventure setname {name}] Changes your name.\n" +
                                "[_adventure {response}] Respond to a question, or do an action. \n" +
                                "[_adventure last] Sends the last sent message. " + "\n```";
                        message.respond().info(reply).queue();
                    } else {
                        String reply = "*Adventure Help!~*\n```ini\n" + "[_adventure help] This list.\n" + "[_adventure actions] List of previous actions.\n" + "[_adventure quit] Ends the adventure.\n" + "[_adventure setname {name}] Changes your name.\n" + "[_adventure {response}] Respond to a question, or do an action.\n" + "[_adventure last] Sends the last sent message.\n" + "[_adventure inventory] Displays your inventory." + "\n```";
                        message.respond().info(reply).queue();
                    }
                    return;
                }
            }
            adventure.parseResponse(message, StringUtils.join(args, " "), false);
        } else {
            adventure.sendLastMessage(message,
                    "**No response given. Use `_adventure help` for a command list.**\nSending last message:");
        }


    }
}
