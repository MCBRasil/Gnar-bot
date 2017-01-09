package xyz.gnarbot.gnar.commands.test;

import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.members.Clearance;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "wow", clearance = Clearance.BOT_MASTER, showInHelp = false)
public class TestCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        //        msg.reply(host.getShard().toString());
        //        msg.reply(host.toString());
        //        msg.reply(this.toString());
        //        //msg.reply(msg.toString());
        //        msg.reply(msg.getAuthor().toString());
        //
        //        msg.reply(String.valueOf(msg.getAuthor().isBotMaster()));
//        Reflections reflections = new Reflections("xyz.gnarbot.gnar.commands");
//
//        StringJoiner joiner = new StringJoiner("\n");
//
//        reflections.getTypesAnnotatedWith(Command.class).parallelStream().forEach(cls -> joiner.add(cls
//                .toGenericString()));
        
        //note.replyRaw(joiner.toString());
    }
}
