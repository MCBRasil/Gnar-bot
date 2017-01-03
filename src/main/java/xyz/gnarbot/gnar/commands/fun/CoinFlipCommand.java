package xyz.gnarbot.gnar.commands.fun;

import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Random;

@Command(aliases = {"coinflip"}, description = "Heads or Tails?")
public class CoinFlipCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        if (new Random().nextInt(2) == 0)
        {
            note.replyEmbedRaw("Coin Flip","**[Heads!]()**");
        }
        else
        {
            note.replyEmbedRaw("Coin Flip","**[Tails!]()**");
        }
    }
}