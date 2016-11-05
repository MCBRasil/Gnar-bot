package xyz.gnarbot.gnar.commands;

import com.google.inject.Inject;
import org.reflections.Reflections;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.StringJoiner;

@Command(aliases = "wow")
public class TestCommand extends CommandExecutor
{
    @Inject
    public Host host;
    
    @Override
    public void execute(Note msg, String label, String[] args)
    {
//        msg.reply(host.getShard().toString());
//        msg.reply(host.toString());
//        msg.reply(this.toString());
//        //msg.reply(msg.toString());
//        msg.reply(msg.getAuthor().toString());
//
//        msg.reply(String.valueOf(msg.getAuthor().isBotMaster()));
        Reflections reflections = new Reflections("xyz.gnarbot.gnar.commands");
    
        StringJoiner joiner = new StringJoiner("\n");
    
        reflections.getTypesAnnotatedWith(Command.class).parallelStream()
                .forEach(cls -> joiner.add(cls.toGenericString()));
        
        msg.replyRaw(joiner.toString());
    }
}
