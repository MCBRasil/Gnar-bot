package xyz.gnarbot.gnar.commands.executors.fun;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.TriviaQuestions;

@Command(aliases = "trivia")
public class TriviaCommand extends CommandExecutor {
    @Override
    public void execute(Note note, String[] args) {
        if (!TriviaQuestions.isSetup()) {
            TriviaQuestions.init();
        }

        if (args.length > 0) {
            note.info(TriviaQuestions.getRandomQuestion(StringUtils.join(args, " ")));
        } else {
            note.error(TriviaQuestions.getRandomQuestion());
        }
    }

}
