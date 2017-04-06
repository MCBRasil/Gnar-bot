package xyz.gnarbot.gnar.commands.handlers;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.servers.Servlet;
import xyz.gnarbot.gnar.servers.Shard;

import java.util.List;

/**
 * Abstract class that is extended when creating a command.
 */
public abstract class CommandExecutor {
    JDA jda;

    Shard shard;

    Servlet servlet;

    CommandHandler commandHandler;

    Bot bot;

    Command commandMeta;

    public JDA getJDA() {
        return jda;
    }

    public Shard getShard() {
        return shard;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public Bot getBot() {
        return bot;
    }

    public Command getMeta() {
        return commandMeta;
    }

    /**
     * Abstract method to be executed when the command is called.
     *  @param message Message object passed into the execution.
     * @param args Arguments passed into the execution.
     */
    protected abstract void execute(Message message, List<String> args);
}