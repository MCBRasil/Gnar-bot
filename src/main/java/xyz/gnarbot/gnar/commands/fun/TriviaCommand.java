package xyz.gnarbot.gnar.commands.fun;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.TriviaQuestions;

@Command(aliases = "trivia")
public class TriviaCommand extends CommandExecutor
{
    
    @Inject
    public Host host;
    
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (!TriviaQuestions.isSetup())
        {
            TriviaQuestions.init();
        }
        
        if (args.length > 0)
        {
            note.info(TriviaQuestions.getRandomQuestion(StringUtils.join(args, " ")));
        }
        else
        {
            note.error(TriviaQuestions.getRandomQuestion());
        }
    }
    
}
