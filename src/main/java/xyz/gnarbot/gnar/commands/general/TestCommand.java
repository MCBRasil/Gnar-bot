package xyz.gnarbot.gnar.commands.general;

import com.google.inject.Inject;
import xyz.gnarbot.gnar.Host;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "wow")
public class TestCommand extends CommandExecutor
{
    @Inject
    public Host host;
    
    @Override
    public void execute(Note msg, String label, String[] args)
    {
        msg.reply(host.getShard().toString());
        msg.reply(host.toString());
        msg.reply(this.toString());
        //msg.reply(msg.toString());
        msg.reply(msg.getAuthor().toString());
    
        msg.reply(String.valueOf(msg.getAuthor().isBotMaster()));
    }
}
