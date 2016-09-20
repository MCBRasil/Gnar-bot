package xyz.gnarbot.commands.general;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import xyz.gnarbot.handlers.commands.CommandExecutor;
import xyz.gnarbot.handlers.commands.CommandHandler;
import xyz.gnarbot.injection.Inject;

public class TestCommand extends CommandExecutor
{
    @Inject
    private CommandHandler commandHandler;
    
    @Inject
    private Guild guild;
    
    @Override
    public void execute(Message msg, String[] args)
    {
        
    }
}
