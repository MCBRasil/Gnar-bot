package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.TriviaQuestions;

import java.util.List;

@Command(
        aliases = "answer",
        category = Category.FUN
)
public class TriviaAnswerCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        if (!TriviaQuestions.isSetup()) {
            TriviaQuestions.init();
        }

        try {
            message.respond().info(TriviaQuestions.getAnswer(Integer.valueOf(args.get(0)))).queue();
        } catch (Exception e) {
            message.respond().error("Please enter a number.").queue();
        }
    }

}
