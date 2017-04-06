package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;

@Command(aliases = {"invite", "invitebot"}, description = "Get a link to invite GN4R to your server.")
public class InviteBotCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        String link = "https://discordapp.com/oauth2/authorize?client_id=201492375653056512&scope=bot&permissions=8";

        message.respond().embed("Get Gnar on your server!")
                .setColor(Constants.COLOR)
                .setDescription("__**[Click to invite Gnar to your server.](" + link + ")**__")
                .rest().queue();
    }
}
