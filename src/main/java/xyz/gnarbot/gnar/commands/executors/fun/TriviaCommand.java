package xyz.gnarbot.gnar.commands.executors.fun;

import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.TriviaQuestions;

import java.util.List;

@Command(aliases = "trivia")
public class TriviaCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        if (!TriviaQuestions.isSetup()) {
            TriviaQuestions.init();
        }

        if (args.size() > 0) {
            note.respond().info(TriviaQuestions.getRandomQuestion()).queue();
        }
    }

}
