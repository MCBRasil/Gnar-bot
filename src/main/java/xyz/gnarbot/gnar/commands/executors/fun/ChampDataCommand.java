package xyz.gnarbot.gnar.commands.executors.fun;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

@Command(aliases = "champdata")
public class ChampDataCommand extends CommandExecutor {
    public static String[] names = ChampQuoteCommand.names;

    @Override
    public void execute(Note note, String[] args) {
        int maybeDistance = 20;
        String maybe = "";

        for (String s : names) {
            int distance = StringUtils.getLevenshteinDistance(s, StringUtils.join(args, "").replaceAll("'", ""));
            if (maybeDistance > distance) {
                maybeDistance = distance;
                maybe = s;
            }
        }

        JSONObject jso = (JSONObject) Utils.information.get(maybe);

        JSONArray spells = jso.getJSONArray("spells");


        String spellInfo = "**" + maybe + "**: " + jso.get("title") + "\n";
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

            spellInfo += "    **" + key + "** - " + spellOne.get("name") + ": \n         " + spellOne.get
                    ("description") + "\n";
        }

        spellInfo += "\n**Skins:**";


        JSONArray skins = jso.getJSONArray("skins");
        for (int i = 0; i < skins.length(); i++) {
            JSONObject j = (JSONObject) skins.get(i);

            int fuckTits = i + 1;

            spellInfo += "\n    **" + fuckTits + "**: " + j.get("name");
        }

        note.replyRaw(spellInfo);
    }

}
