package xyz.gnarbot.gnar.commands.executors.fun;

import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;
import java.util.Random;

@Command(aliases = {"coinflip", "flip"}, description = "Heads or Tails?")
public class CoinFlipCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
        note.respond().embed("Coin Flip")
                .setColor(Constants.COLOR)
                .setDescription(new Random().nextInt(2) == 0 ? "Heads" : "Tails!")
                .rest().queue();
    }
}