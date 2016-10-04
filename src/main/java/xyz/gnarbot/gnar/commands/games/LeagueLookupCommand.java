package xyz.gnarbot.gnar.commands.games;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.util.StringJoiner;

@Command(
        aliases = {"league", "lol"},
        usage = "(LOL Username)",
        description = "Look up Leauge of Legends statistics of a player."
)
public class LeagueLookupCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        StringJoiner joiner = new StringJoiner("");
        try
        {
            JSONObject jso = Utils.jsonFromUrl("https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/"
                    + StringUtils.join(args, "").toLowerCase() + "?api_key=" + Bot.INSTANCE.getAuthTokens().getProperty("leauge"));
            JSONObject info = jso.getJSONObject(args[0]);
            joiner.add("**League Of Legends** Account Info: ");
            joiner.add("Season: **SEASON 2016** \n");
            joiner.add("Current Name: **" + info.get("name") + "**");
            joiner.add("ID: **" + info.get("id") + "**");
            joiner.add("Level: **" + info.get("summonerLevel") + "**");
            joiner.add("Icon ID: **" + info.get("profileIconId") + "**");

            JSONObject jso2 = Utils.jsonFromUrl(
                    "https://na.api.pvp.net/api/lol/na/v1.3/stats/by-summoner/" + info.get("id")
                            + "/summary?season=SEASON2016&api_key=RGAPI-4A230644-F1D8-40B7-A81E-4566E145BA1C");
            JSONArray j = jso2.getJSONArray("playerStatSummaries");
            JSONObject fin = j.getJSONObject(0);

            joiner.add("Wins: **" + fin.get("wins") + "**");

            JSONObject stats = (JSONObject) fin.get("aggregatedStats");

            joiner.add("Champion Kills: **" + stats.get("totalChampionKills") + "**");
            joiner.add("Minion Kills: **" + stats.get("totalMinionKills") + "**");
            joiner.add("Assists: **" + stats.get("totalAssists") + "**");
            joiner.add("Neutral Minion Kills: **" + stats.get("totalNeutralMinionsKilled") + "**");
            joiner.add("Turrets Killed: **" + stats.get("totalTurretsKilled") + "**");

            note.getChannel().sendMessage(joiner.toString());
        }
        catch (Exception e)
        {
            note.getChannel()
                    .sendMessage("Account not found or there was an error connecting to the LoL API\n"
                            + "Here is what I was able to get: ");
            try
            {
                note.getChannel().sendMessage(joiner.toString());
            }
            catch (Exception ignore) {}
        }
        
//        MessageBuilder mb = new MessageBuilder();
//        try
//        {
//            JSONObject jso = Utils.readJsonFromUrl("https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/"
//                    + StringUtils.join(args, "").toLowerCase() + "?api_key=" + Bot.INSTANCE.getAuthTokens().getProperty("leauge"));
//            JSONObject info = jso.getJSONObject(args[0]);
//            joiner.add("**League Of Legends** Account Info: ");
//            joiner.add("Season: **SEASON 2016** \n");
//            joiner.add("Current Name: **" + info.get("name") + "**");
//            joiner.add("ID: **" + info.get("id") + "**");
//            joiner.add("Level: **" + info.get("summonerLevel") + "**");
//            joiner.add("Icon ID: **" + info.get("profileIconId") + "**");
//
//            try
//            {
//                JSONObject jso2 = Utils.readJsonFromUrl(
//                        "https://na.api.pvp.net/api/lol/na/v1.3/stats/by-summoner/" + info.get("id")
//                                + "/summary?season=SEASON2016&api_key=RGAPI-4A230644-F1D8-40B7-A81E-4566E145BA1C");
//                JSONArray j = jso2.getJSONArray("playerStatSummaries");
//                JSONObject fin = j.getJSONObject(0);
//
//                joiner.add("Wins: **" + fin.get("wins") + "**");
//
//                JSONObject stats = (JSONObject) fin.get("aggregatedStats");
//
//                joiner.add("Champion Kills: **" + stats.get("totalChampionKills") + "**");
//                joiner.add("Minion Kills: **" + stats.get("totalMinionKills") + "**");
//                joiner.add("Assists: **" + stats.get("totalAssists") + "**");
//                joiner.add("Neutral Minion Kills: **" + stats.get("totalNeutralMinionsKilled") + "**");
//                joiner.add("Turrets Killed: **" + stats.get("totalTurretsKilled") + "**");
//
//                message.getChannel().sendMessage(mb.build());
//            }
//            catch (Exception e)
//            {
//                message.getChannel()
//                        .sendMessage("Account not found or there was an error connecting to the LoL API\n"
//                                + "Here is what I was able to get: ");
//                try
//                {
//                    message.getChannel().sendMessage(mb.build());
//                }
//                catch (Exception ex) {e.printStackTrace();}
//                //e.printStackTrace();
//            }
//        }
//        catch (Exception e)
//        {
//            message.getChannel()
//                    .sendMessage("Account not found or there was an error connecting to the LoL API\n"
//                            + "Here is what I was able to get: ");
//            try
//            {
//                message.getChannel().sendMessage(mb.build());
//            }
//            catch (Exception ignore) {}
//        }
    }
}
