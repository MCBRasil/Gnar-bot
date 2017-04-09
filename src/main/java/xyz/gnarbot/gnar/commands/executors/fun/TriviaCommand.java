package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.TriviaQuestions;

@Command(
        aliases = "trivia",
        category = Category.FUN
)
public class TriviaCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        if (!TriviaQuestions.isSetup()) {
            TriviaQuestions.init();
        }

        if (args.length > 0) {
            message.respond().info(TriviaQuestions.getRandomQuestion()).queue();
        }
    }

}
