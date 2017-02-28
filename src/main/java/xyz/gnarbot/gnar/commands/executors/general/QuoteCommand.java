package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.EmbedCreator;
import xyz.gnarbot.gnar.utils.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(aliases = {"quote", "quotemsg"},
        usage = "(message id) [#channel]",
        description = "Quote somebody else..")
public class QuoteCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.isEmpty()) {
            note.error("Provide a message id.").queue();
            return;
        }

        TextChannel targetChannel = note.getTextChannel();
        if (note.getMentionedChannels().size() > 0) {
            targetChannel = note.getMentionedChannels().get(0);
        }

        List<Message> toDelete = new ArrayList<>();

        for (String id : args) {
            if (!id.contains("#")) {
                try {
                    Message msg = note.getChannel().getMessageById(id).complete();

                    new EmbedCreator(note.getServlet(), targetChannel)
                            .description(msg.getContent())
                            .author(msg.getAuthor())
                            .rest().queue();

                } catch (Exception e) {
                    try {
                        Message m = note
                                .error("Could not find a message with the ID " + id + " within this channel.")
                                .complete();
                        toDelete.add(m);
                    } catch (Exception ignore) {}
                }
            }
        }

        toDelete.add(note);

        try {
            Message m = note.info("Sent quotes to the " + targetChannel.getName() + " channel!").complete();

            toDelete.add(m);

            Bot.INSTANCE.getScheduler().schedule(() -> {
                for (Message m2 : toDelete) {
                    m2.delete().queue();
                }
            }, 5, TimeUnit.SECONDS);

        } catch (Exception ignore) {}
    }
}



