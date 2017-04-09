package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.StringJoiner;

@Command(
        aliases = {"poop"},
        usage = "[string]",
        description = "Shit your heart out.",
        category = Category.FUN)
public class PoopCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        String poop = StringUtils.join(args, " ");

        StringJoiner joiner = new StringJoiner("\n", "```\n", "```");

        joiner.add("░░░░░░░░░░░█▀▀░░█░░░░░░");
        joiner.add("░░░░░░▄▀▀▀▀░░░░░█▄▄░░░░");
        joiner.add("░░░░░░█░█░░░░░░░░░░▐░░░");
        joiner.add("░░░░░░▐▐░░░░░░░░░▄░▐░░░");
        joiner.add("░░░░░░█░░░░░░░░▄▀▀░▐░░░");
        joiner.add("░░░░▄▀░░░░░░░░▐░▄▄▀░░░░");
        joiner.add("░░▄▀░░░▐░░░░░█▄▀░▐░░░░░");
        joiner.add("░░█░░░▐░░░░░░░░▄░▌░░░░░");
        joiner.add("░░░█▄░░▀▄░░░░▄▀█░▌░░░░░");
        joiner.add("░░░▌▐▀▀▀░▀▀▀▀░░█░▌░░░░░");
        joiner.add("░░▐▌▐▄░░▀▄░░░░░█░█▄▄░░░");

        StringBuilder poopArt = new StringBuilder("░░░▀▀░▄███▄▄░░░▀▄▄▄▀░░░");

        for (int i = 0; i < poop.length(); i++) {
            try {
                poopArt.setCharAt(7 + i, poop.charAt(i));
            } catch (IndexOutOfBoundsException e) {
                message.respond().error("Poop is too big. Constipation occurred.").queue();
                return;
            }
        }

        joiner.add(poopArt);
        joiner.add("░░░░░░░░░░░░░░░░░░░░░░░");

        message.respond().embed("Pooping Memes")
                .setColor(Constants.COLOR)
                .setDescription(joiner.toString())
                .rest().queue();
    }
}