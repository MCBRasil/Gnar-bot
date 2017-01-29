package xyz.gnarbot.gnar.commands.executors.fun;

import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.TriviaQuestions;

@Command(aliases = "answer")
public class TriviaAnswerCommand extends CommandExecutor {
    @Override
    public void execute(Note note, String[] args) {
        if (!TriviaQuestions.isSetup()) {
            TriviaQuestions.init();
        }

        try {
            note.info(TriviaQuestions.getAnswer(Integer.valueOf(args[0])));
        } catch (Exception e) {
            note.error("Please enter a number.");
        }
    }

}
