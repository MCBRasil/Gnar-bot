package xyz.gnarbot.gnar.commands.handlers;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.members.Client;
import xyz.gnarbot.gnar.servers.Servlet;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler {
    private final Bot bot;
    private final Servlet servlet;

    public List<String> disabled = new ArrayList<>();
    private int requests = 0;

    public CommandHandler(Servlet servlet, Bot bot) {
        this.servlet = servlet;
        this.bot = bot;
    }

    /**
     * Call the command based on the message content.
     *
     * @param message Message object.
     * @param content String content of the message.
     * @param author  Author of the message.
     */
    public void callCommand(Message message, String content, Client author) {
        if (!content.startsWith(bot.getToken())) return;

        // Tokenize the message.
        List<String> tokens = Utils.fastSplit(content, ' ');

        String label = tokens.get(0).substring(bot.getToken().length()).toLowerCase();

        List<String> args = tokens.subList(1, tokens.size());

        Note note = new Note(servlet, message);

        CommandRegistry.CommandEntry entry =  bot.getCommandRegistry().getEntry(label);

        if (entry == null) return;

        Class<? extends CommandExecutor> cls = entry.cls;

        Command meta = entry.meta;

        if (!servlet.getSelfClient().hasPermission(meta.permissions())) {
            note.respond().error("Gnar doesn't have the permissions: `"
                    + Arrays.stream(meta.permissions())
                            .map(Permission::getName)
                            .collect(Collectors.toList())
                    + "`");
            return;
        } else if (!author.hasPermission(meta.permissions())) {
            note.respond().error("You have insufficient permissions. `"
                    + Arrays.stream(meta.permissions())
                            .map(Permission::getName)
                            .collect(Collectors.toList())
                    + "`");
            return;
        }

        if (meta.level().getValue() > author.getLevel().getValue()) {
            note.respond().error("Insufficient bot level.\n" + meta.level().getRequireText());
            return;
        }

        try {
            requests++;
            CommandExecutor cmd = cls.newInstance();

            cmd.jda = servlet.getJDA();
            cmd.shard = servlet.getShard();
            cmd.servlet = servlet;
            cmd.bot = bot;
            cmd.commandMeta = meta;

            cmd.execute(note, args);
        } catch (RuntimeException e) {
            note.respond().error("**Exception**: " + e.getMessage()).queue();
            e.printStackTrace();
        } catch (IllegalAccessException | InstantiationException e) {
            note.respond().error("**Class Instantiation Failed**: " + e.getMessage()).queue();
            e.printStackTrace();
        }
    }

    /**
     * Return the amount of successful requests on this command handler.
     *
     * @return the amount of successful requests on this command handler.
     */
    public int getRequests() {
        return requests;
    }
}
