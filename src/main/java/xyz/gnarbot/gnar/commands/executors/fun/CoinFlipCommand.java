package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;
import java.util.Random;

@Command(aliases = {"coinflip", "flip"}, description = "Heads or Tails?", category = Category.FUN)
public class CoinFlipCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        message.respond().embed("Coin Flip")
                .setColor(Constants.COLOR)
                .setDescription(new Random().nextInt(2) == 0 ? "Heads" : "Tails!")
                .rest().queue();
    }
}