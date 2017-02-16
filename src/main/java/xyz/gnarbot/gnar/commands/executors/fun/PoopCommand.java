package xyz.gnarbot.gnar.commands.executors.fun;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.util.List;
import java.util.StringJoiner;

@Command(aliases = {"poop"}, usage = "[string]", description = "Shit your heart out.")
public class PoopCommand extends CommandExecutor {
    @Override
    public void execute(Note note, List<String> args) {
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
                note.error("Poop is too big. Constipation occurred.");
                return;
            }
        }

        joiner.add(poopArt);
        joiner.add("░░░░░░░░░░░░░░░░░░░░░░░");

        note.respond("Pooping Memes", joiner.toString());
    }
}