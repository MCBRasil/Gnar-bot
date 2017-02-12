package xyz.gnarbot.gnar.commands.executors.test;

import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Level;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;

@Command(aliases = "wow", level = Level.BOT_CREATOR, showInHelp = false)
public class TestCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
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
