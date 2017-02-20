package xyz.gnarbot.gnar.textadventure;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = {"adventure", "adv", "pokemon"}, usage = "(argument)", description = "Respond to Adventure things!")
public class AdventureCommand extends CommandExecutor {
    @SuppressWarnings("all")
    @Override
    public void execute(Note note, List<String> args) {
        if (!Adventure.hasAdventure(note.getAuthor())) {
            note.info("Use `_startadventure` to begin your adventure!");
            return;
        }
        Adventure adventure = Adventure.getAdventure(note.getAuthor(), note);

        if (args.size() > 0) {
            switch (args.get(0).toLowerCase()) {
                case "quit": {
                    note.info("Poof! Your adventure has been lost to the winds of Azaroth.");
                    Adventure.removeAdventure(note.getAuthor());
                    return;
                }
                case "map": {
                    note.info("Preparing your map...");
                    adventure.getGrid().sendMap(note);
                    return;
                }
                case "inventory": {
                    if (adventure.getInventory() != null) {
                        adventure.sendInformativeMessage(note, adventure.getInventory().toString());
                        return;
                    }
                }
                case "setname": {
                    if (args.size() > 1) {
                        String oldname = adventure.getHeroName();
                        adventure.setHeroName(StringUtils.join(args, " ").replace("setname ", ""));
                        adventure.sendInformativeMessage(note,
                                ":bulb: The hero wishes to change their name? Very well " +
                                        "then . I shall now refer to you as **" + adventure
                                        .getHeroName() + "**");
                        adventure.logAction("Changed Hero name to " + adventure.getHeroName() + " from " + oldname);
                        return;
                    } else {
                        adventure.sendInformativeMessage(note,
                                ":bulb: Hmm... I can't do that unless you tell me what to " +
                                        "call you, Hero.");
                        return;
                    }
                }
                case "actions": {
                    if (adventure.getActionList().size() == 0) {
                        adventure.sendInformativeMessage(note, "I couldn't find anything... Sorry :no_mouth:");
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
                            adventure.sendInformativeMessage(note, r);
                            return;
                        }
                    }
                    r += "```";
                    adventure.sendInformativeMessage(note, r);
                    return;
                }
                case "last": {
                    adventure.sendLastMessage(note, "Sending last message as per your request~");
                    return;
                }
                case "help": {
                    if (adventure.getInventory() == null) {
                        String reply = "*Adventure Help!~*\n```ini\n" + "[_adventure help] This list.\n" + "[_adventure " +
                                "actions] List of previous actions.\n" + "[_adventure quit] Ends the adventure.\n" +
                                "[_adventure setname {name}] Changes your name.\n" +
                                "[_adventure {response}] Respond to a question, or do an action. \n" +
                                "[_adventure last] Sends the last sent message. " + "\n```";
                        note.info(reply);
                    } else {
                        String reply = "*Adventure Help!~*\n```ini\n" + "[_adventure help] This list.\n" + "[_adventure actions] List of previous actions.\n" + "[_adventure quit] Ends the adventure.\n" + "[_adventure setname {name}] Changes your name.\n" + "[_adventure {response}] Respond to a question, or do an action.\n" + "[_adventure last] Sends the last sent message.\n" + "[_adventure inventory] Displays your inventory." + "\n```";
                        note.info(reply);
                    }
                    return;
                }
            }
            adventure.parseResponse(note, StringUtils.join(args, " "), false);
        } else {
            adventure.sendLastMessage(note,
                    "**No response given. Use `_adventure help` for a command list.**\nSending last message:");
        }


    }
}
