package xyz.gnarbot.gnar.commands.executors.fun;

import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;
import java.util.Random;

@Command(aliases = "8ball", usage = "-question", description = "Test your wildest dreams!")
public class EightBallCommand extends CommandExecutor {
    private final Random random = new Random();

    private final String[] responses = {"It is certain", "It is decidedly so", "Without a doubt", "Yes, definitely",
            "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes",
            "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now",
            "Concentrate and ask again", "Don't count on it", "My reply is no", "My sources say no",
            "Outlook not so good", "Very doubtful"};

    @Override
    public void execute(Note note, List<String> args) {
        if (args.isEmpty()) {
            note.respond().error("Ask the 8-ball something.").queue();
            return;
        }

        note.respond().embed("8-Ball")
                .setColor(Constants.COLOR)
                .setDescription(responses[random.nextInt(responses.length)])
                .rest().queue();
    }
}
