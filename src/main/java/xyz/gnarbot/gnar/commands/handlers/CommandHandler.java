package xyz.gnarbot.gnar.commands.handlers;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.SelfUser;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.members.Person;
import xyz.gnarbot.gnar.members.PersonHandler;
import xyz.gnarbot.gnar.servers.Host;
import xyz.gnarbot.gnar.servers.Shard;
import xyz.gnarbot.gnar.servers.music.MusicManager;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {
    private final Host host;
    private final Injector injector;

    public List<String> disabled = new ArrayList<>();
    private int requests = 0;

    public CommandHandler(Host host) {
        this.host = host;
        this.injector = Guice.createInjector(new CommandModule());
    }

    /**
     * Call the command based on the message content.
     *
     * @param message Message object.
     * @param content String content of the message.
     * @param author Author of the message.
     */
    public void callCommand(Message message, String content, Person author) {
        if (!content.startsWith(Bot.getToken())) return;

        // Tokenize the message.
        List<String> tokens = Utils.fastSplit(content, ' ');

        String label = tokens.get(0).substring(Bot.getToken().length()).toLowerCase();

        List<String> args = tokens.subList(1, tokens.size());

        Note note = new Note(host, message);

        CommandExecutor cmd = host.getShard().getCommandRegistry().getCommand(label);

        if (cmd == null) return;

        if (cmd.getLevel().getValue() > author.getLevel().getValue()) {
            note.error("Insufficient bot level. Requires `" + cmd.getLevel().getTitle() + "` or higher.");
            return;
        }

        try {
            requests++;
            cmd.syncExecute(injector, note, args);
        } catch (RuntimeException e) {
            note.error("**Exception**: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
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

    /**
     * Injector module for commands.
     */
    public class CommandModule extends AbstractModule {
        @Override
        protected void configure() {
            // INSTANCES
            bind(SelfUser.class).toInstance(host.getShard().getSelfUser());
            bind(Host.class).toInstance(host);
            bind(Shard.class).toInstance(host.getShard());

            bind(CommandHandler.class).toInstance(CommandHandler.this);
            bind(PersonHandler.class).toInstance(host.getPersonHandler());

            bind(JDA.class).toInstance(host.getJDA());

            // PROVIDERS
            bind(MusicManager.class).toProvider(host::getMusicManager);
        }
    }
}
