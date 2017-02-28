package xyz.gnarbot.gnar.commands.handlers;

import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

/**
 * Abstract class that is extended when creating a command.
 */
public abstract class CommandExecutor {
    Bot bot;

    Command commandMeta;

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