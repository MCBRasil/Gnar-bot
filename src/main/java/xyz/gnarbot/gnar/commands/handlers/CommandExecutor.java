package xyz.gnarbot.gnar.commands.handlers;

import net.dv8tion.jda.core.JDA;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.servers.Servlet;
import xyz.gnarbot.gnar.servers.Shard;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

/**
 * Abstract class that is extended when creating a command.
 */
public abstract class CommandExecutor {
    JDA jda;

    Shard shard;

    Servlet servlet;

    Bot bot;

    Command commandMeta;

    public JDA jda() {
        return jda;
    }

    public Shard shard() {
        return shard;
    }

    public Servlet servlet() {
        return servlet;
    }

    public Bot bot() {
        return bot;
    }

    public Command meta() {
        return commandMeta;
    }

    /**
     * Abstract method to be executed when the command is called.
     *
     * @param note Message object passed into the execution.
     * @param args Arguments passed into the execution.
     */
    protected abstract void execute(Note note, List<String> args);
}