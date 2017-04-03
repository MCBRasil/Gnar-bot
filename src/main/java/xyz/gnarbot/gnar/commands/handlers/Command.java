package xyz.gnarbot.gnar.commands.handlers;

import net.dv8tion.jda.core.Permission;
import xyz.gnarbot.gnar.members.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stores the initial data of a command class upon instantiation.
 *
 * @see CommandExecutor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    /**
     * @return The aliases of this command.
     */
    String[] aliases();

    /**
     * @return The description of this command.
     */
    String description() default "No description provided.";

    /**
     * @return The usage of this command.
     */
    String usage() default "";

    /**
     * @return The symbol of this command.
     */
    String symbol() default "";

    /**
     * @return Flag to show this command in help.
     */
    boolean showInHelp() default true;

    /**
     * @return The permission required for this command.
     */
    Level level() default Level.USER;

    Permission[] permissions() default {};
}
