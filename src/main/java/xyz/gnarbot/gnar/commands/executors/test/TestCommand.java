package xyz.gnarbot.gnar.commands.executors.test;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;

@Command(aliases = "wow", administrator = true, category = Category.NONE)
public class TestCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
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
