package xyz.gnarbot.commands.general;

import com.google.inject.Inject;
import net.dv8tion.jda.entities.Message;
import xyz.gnarbot.handlers.commands.CommandExecutor;

public class TestCommand extends CommandExecutor
{
    @Inject
    public String id;
    
    @Override
    public void execute(Message msg, String label, String[] args)
    {
        
    }
}
