package xyz.gnarbot.gnar.commands.executors.general;

import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = {"invite", "invitebot"}, description = "Get a link to invite GN4R to your server.")
public class InviteBotCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        String tempLink = "https://discordapp.com/oauth2/authorize?client_id=201492375653056512&scope=bot&permissions" +
                "=8";

        note.respond("Want some Gnar?!", "__**[Click to invite Gnar to your server.](" + tempLink + ")**__",
                Bot.getColor());
    }
}
