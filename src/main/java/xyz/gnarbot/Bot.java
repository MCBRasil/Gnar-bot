package xyz.gnarbot;


import com.google.inject.Guice;
import com.google.inject.Injector;
import xyz.gnarbot.commands.general.TestCommand;
import xyz.gnarbot.injection.ShardInjectorModule;

/**
 * Main class of the bot.
 */
public class Bot
{
    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(new ShardInjectorModule(new Shard()));
        
        TestCommand command = new TestCommand();
        
        injector.injectMembers(command);
    
        System.out.println(command.id);
    }
}

