package xyz.gnarbot.gnar.commands.executors.fun;

import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Command(
        aliases = "champdata",
        category = Category.FUN)
public class ChampDataCommand extends CommandExecutor {
    private static final String[] names = ChampQuoteCommand.names;
    private static JSONObject information;

    static {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("_DATA/league/League.txt")));

            StringBuilder info = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                info.append(line);
            }

            information = new JSONObject(info.toString());
        } catch (Exception ignore) { }
    }

    @Override
    public void execute(Message message, String[] args) {
        int maybeDistance = 20;
        String maybe = "";

        for (String s : names) {
            int distance = StringUtils.getLevenshteinDistance(s, StringUtils.join(args, "").replaceAll("'", ""));
            if (maybeDistance > distance) {
                maybeDistance = distance;
                maybe = s;
            }
        }

        JSONObject jso = information.getJSONObject(maybe);

        JSONArray spells = jso.getJSONArray("spells");


        StringBuilder spellInfo = new StringBuilder("**" + maybe + "**: " + jso.get("title") + "\n");
        String key = "";


        for (int i = 0; i < spells.length(); i++) {
            JSONObject spellOne = (JSONObject) spells.get(i);

            switch (i) {
                case 0:
                    key = "Q";
                    break;
                case 1:
                    key = "W";
                    break;
                case 2:
                    key = "E";
                    break;
                case 3:
                    key = "R";
                    break;
            }

            spellInfo.append("    **").append(key).append("** - ").append(spellOne.get("name")).append(": \n         ").append(spellOne.get
                    ("description")).append("\n");
        }

        spellInfo.append("\n**Skins:**");


        JSONArray skins = jso.getJSONArray("skins");
        for (int i = 0; i < skins.length(); i++) {
            JSONObject j = (JSONObject) skins.get(i);

            int fuckTits = i + 1;

            spellInfo.append("\n    **").append(fuckTits).append("**: ").append(j.get("name"));
        }

        message.respond().text(spellInfo.toString()).queue();
    }

}
