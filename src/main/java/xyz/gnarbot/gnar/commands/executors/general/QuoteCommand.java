package xyz.gnarbot.gnar.commands.executors.general;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.KUtils;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(aliases = {"quote", "quotemsg"},
        usage = "-msg_id [-channel_in_#_format]",
        description = "Quote somebody else..")
public class QuoteCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (args.isEmpty()) {
            note.error("Provide a message id.");
            return;
        }

        try {
            String last = args.get(args.size() - 1);
            TextChannel targetChannel = note.getTextChannel();
            if (note.getMentionedChannels().size() > 0){
                targetChannel = note.getMentionedChannels().get(0);
            }
            for (String id : args) {
                 if (!id.contains("#")) {
                     Message msg = note.getChannel().getMessageById(id).complete();
                     targetChannel.sendMessage(KUtils.makeEmbed(null,
                             msg.getContent(), Bot.getColor(),
                             msg.getAuthor().getAvatarUrl(), null,
                             note.getHost().getPersonHandler().asPerson(msg.getAuthor()))).queue();
                 }
            }
            Message m = note.getChannel().sendMessage(KUtils.makeEmbed("Quote Messages", "Sent quotes to the " + targetChannel.getName() + " channel!")).complete();
            TimeUnit.SECONDS.sleep(5);
            m.deleteMessage().complete();
        } catch (Exception e) {
            e.printStackTrace();
            note.error("Could not find that message within this channel.");
        }
    }
}



