package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;

@Command(
        aliases = "goodshit",
        description = "go౦ԁ sHit\uD83D\uDC4C.",
        category = Category.FUN)
public class GoodShitCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        message.respond().text("\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D" +
                "\uDC40\uD83D\uDC4C\uD83D\uDC40 good shit go౦ԁ sHit\uD83D\uDC4C thats ✔ some " +
                "good\uD83D\uDC4C\uD83D\uDC4Cshit " +
                "right\uD83D\uDC4C\uD83D\uDC4Cthere\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4C right✔there ✔✔if i do ƽaү so " +
                "" + "my self \uD83D\uDCAF i say so \uD83D\uDCAF thats what im talking about right there right there " +
                "" + "(chorus: ʳᶦᵍʰᵗ ᵗʰᵉʳᵉ) mMMMMᎷМ\uD83D\uDCAF \uD83D\uDC4C\uD83D\uDC4C " +
                "\uD83D\uDC4CНO0ОଠOOOOOОଠଠOoooᵒᵒᵒᵒᵒᵒᵒᵒᵒ\uD83D\uDC4C \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4C " +
                "\uD83D\uDCAF \uD83D\uDC4C \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC4C\uD83D\uDC4CGood shit")
                .queue();

    }
}
