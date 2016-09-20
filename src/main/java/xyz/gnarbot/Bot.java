package xyz.gnarbot;

import xyz.gnarbot.commands.general.TestCommand;
import xyz.gnarbot.handlers.commands.CommandHandler;
import xyz.gnarbot.injection.InjectHandler;

import java.lang.reflect.Field;

/**
 * Main class of the bot.
 */
public class Bot
{
    public static void main(String[] args)
    {
        Field[] fields = InjectHandler.findFields(TestCommand.class);
        
        for (Field field : fields)
        {
            System.out.println(field.getType());
        }
        
        InjectHandler.injectFields(new TestCommand(), new CommandHandler());
    }
}
