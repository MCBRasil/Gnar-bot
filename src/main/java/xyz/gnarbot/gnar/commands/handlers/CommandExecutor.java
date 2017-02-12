package xyz.gnarbot.gnar.commands.handlers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import xyz.gnarbot.gnar.members.Level;
import xyz.gnarbot.gnar.utils.Note;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class that is extended when creating a command.
 */
public abstract class CommandExecutor {
    private String[] aliases;

    private String description = "No description provided.";

    private String usage = "No usage provided.";

    private Level level = Level.USER;

    private String symbol;

    private boolean shownInHelp = true;

    private boolean inject = false;

    /**
     * Synchronized method to inject dependencies and then run the
     * {@link CommandExecutor#execute(Note, List)} method.
     * @param injector Dependency injector.
     * @param note Note wrapper.
     * @param args Command arguments.
     */
    public synchronized void syncExecute(Injector injector, Note note, List<String> args) throws IllegalAccessException {
        if (injector != null && inject) injector.injectMembers(this);

        execute(note, args);

        // CLEAN UP
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                field.set(this, null);
            }
        }
    }

    /**
     * Abstract method to be executed when the command is called.
     *
     * @param note Message object passed into the execution.
     * @param args Arguments passed into the execution.
     */
    protected abstract void execute(Note note, List<String> args);

    /**
     * @return The aliases of the command.
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * @param aliases Varargs list of aliases.
     */
    public void setAliases(String... aliases) {
        this.aliases = aliases;
    }

    /**
     * @return The description of the command.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param desc New description.
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * @return The usage message of the command.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * @param usage New usage message.
     */
    public void setUsage(String usage) {
        this.usage = usage;
    }

    /**
     * @return The permission required to execute the command.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @param perm New permission required.
     */
    public void setLevel(Level perm) {
        this.level = perm;
    }

    /**
     * @return Symbol of the command for _help.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol New symbol of the command.
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return Flag to show this command in help.
     */
    public boolean isShownInHelp() {
        return shownInHelp;
    }

    /**
     * @param shown Is the command going to be shown in help?
     */
    public void setShownInHelp(boolean shown) {
        this.shownInHelp = shown;
    }

    /**
     * @return if the command have fields that needs
     * to be injected.
     */
    public boolean isInject() {
        return this.inject;
    }

    /**
     * Set if the command have fields that needs
     * to be injected.
     */
    public void setInject(boolean inject) {
        this.inject = inject;
    }

    /**
     * @return String representation of the command.
     */
    @Override
    public String toString() {
        return this.getClass()
                .getSimpleName() + "(aliases=" + Arrays.toString(aliases) + ", desc=\"" + description + "\", " +
                "usage=\"" + usage + "\", level=" + level + ", shownInHelp=" + shownInHelp + ")";
    }
}