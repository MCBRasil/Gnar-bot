package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.KUtils;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = {"quote", "quotemsg"},
        usage = "-msg_id",
        description = "Quote somebody else..")
public class QuoteCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.isEmpty()) {
            note.error("Provide a message id.");
            return;
        }

        try {
            for (String id : args) {
                Message msg = note.getChannel().getMessageById(id).complete();

                note.getChannel().sendMessage(KUtils.makeEmbed(null,
                        msg.getContent(), Bot.getColor(),
                        msg.getAuthor().getAvatarUrl(), null,
                        note.getHost().getPersonHandler().asPerson(msg.getAuthor()))).queue();
            }
        } catch (Exception e) {
            note.error("Could not find that message within this channel.");
        }
    }
}



