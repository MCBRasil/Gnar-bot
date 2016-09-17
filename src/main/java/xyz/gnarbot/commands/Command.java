package xyz.gnarbot.commands;

import xyz.gnarbot.PermissionLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command
{
    String[] aliases();
    
    String description() default "No descriptions provided.";
    
    String usage() default "";
    
    boolean showInHelp() default true;

    PermissionLevel permissionRequired() default PermissionLevel.USER;
}
