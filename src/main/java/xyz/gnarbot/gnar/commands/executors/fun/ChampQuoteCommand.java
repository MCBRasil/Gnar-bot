package xyz.gnarbot.gnar.commands.executors.fun;

import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Command(aliases = {"q", "quotes"})
public class ChampQuoteCommand extends CommandExecutor {
    public static final String[] names = {"Aatrox", "Ahri", "Akali", "Alistar", "Amumu", "Anivia", "Annie", "Ashe",
            "Aurelion Sol", "Azir", "Bard", "Blitzcrank", "Brand", "Braum", "Caitlyn", "Cassiopeia", "Cho'Gath",
            "Corki", "Darius", "Diana", "Dr. Mundo", "Draven", "Ekko", "Elise", "Evelynn", "Ezreal", "Fiddlesticks",
            "Fiora", "Fizz", "Galio", "Gangplank", "Garen", "Gnar", "Gragas", "Graves", "Hecarim", "Heimerdinger",
            "Illaoi", "Irelia", "Janna", "Jarvan IV", "Jax", "Jayce", "Jhin", "Jinx", "Kalista", "Karma", "Karthus",
            "Kassadin", "Katarina", "Kayle", "Kennen", "Kha'Zix", "Kindred", "Kled", "Kog'Maw", "LeBlanc", "Lee Sin",
            "Leona", "Lissandra", "Lucian", "Lulu", "Lux", "Malphite", "Malzahar", "Maokai", "Master Yi", "Miss " +
            "Fortune", "Mordekaiser", "Morgana", "Nami", "Nasus", "Nautilus", "Nidalee", "Nocturne", "Nunu", "Olaf",
            "Orianna", "Pantheon", "Poppy", "Quinn", "Rammus", "Rek'Sai", "Renekton", "Rengar", "Riven", "Rumble",
            "Ryze", "Sejuani", "Shaco", "Shen", "Shyvana", "Singed", "Sion", "Sivir", "Skarner", "Sona", "Soraka",
            "Swain", "Syndra", "Tahm Kench", "Taliyah", "Talon", "Taric", "Teemo", "Thresh", "Tristana", "Trundle",
            "Tryndamere", "Twisted Fate", "Twitch", "Udyr", "Urgot", "Varus", "Vayne", "Veigar", "Vel'Koz", "Vi",
            "Viktor", "Vladimir", "Volibear", "Warwick", "Wukong", "Xerath", "Xin", "Zhao", "Yasuo", "Yorick", "Zac",
            "Zed", "Ziggs", "Zilean", "Zyra"};

    @Override
    public void execute(Note note, List<String> args) {
        try {
            //Makes the first character uppercase
            String champ = StringUtils.join(args, " ");
            try {
                char[] stringArray = champ.trim().toCharArray();
                stringArray[0] = Character.toUpperCase(stringArray[0]);
                champ = new String(stringArray);
            } catch (Exception ignore) {}

            //Champions List
            if (champ.equalsIgnoreCase("list")) {
                String championsList = "**Current Champions:** \n";
                for (String s : names) {
                    championsList += ", " + s;
                }
                note.reply(championsList.replaceFirst(", ", "")).queue();
                return;
            }

            if (champ.equals("")) champ = "Gnar";

            BufferedReader br;

            ArrayList<String> quotes = new ArrayList<>();
            try {
                //They typed the name correctly
                br = new BufferedReader(new FileReader(new File("_DATA/quotes/" + champ + ".txt")));

            } catch (Exception e) {
                //They made a typo
                int maybeDistance = 20;
                String maybe = "";

                for (String s : names) {
                    int distance = StringUtils.getLevenshteinDistance(s, champ);
                    if (maybeDistance > distance) {
                        maybeDistance = distance;
                        maybe = s;
                    }
                }

                br = new BufferedReader(new FileReader(new File("_DATA/quotes/" + maybe + ".txt")));

                note.respond("Champion Quotes", "I think you meant **" + maybe + "**? Here's a quote from them!").queue();
                champ = maybe;
            }

            //Gather all quotes
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.equals("")) quotes.add(line);
            }

            //Pull a random quote and send
            note.reply("`" + quotes.get(new Random().nextInt(quotes.size())) + "` - **" + champ + "**").queue();

        } catch (Exception e) {
            note.error("Odd, you should have never got here. For getting here and some how butchering that " +
                    "champions name so bad, here's a cookie :cookie:").queue();
            e.printStackTrace();
        }
    }

}
