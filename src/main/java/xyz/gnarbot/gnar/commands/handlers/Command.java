package xyz.gnarbot.gnar.commands.handlers;

import net.dv8tion.jda.core.Permission;

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

    boolean serverOwner() default false;

    boolean administrator() default false;

    Category category() default Category.GENERAL;

    Permission[] guildPermissions() default {};

    Permission[] channelPermissions() default {};

    Permission[] voicePermissions() default {};
}
