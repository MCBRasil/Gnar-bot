package xyz.gnarbot.gnar.commands.executors.games;

import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = {"league", "lol"},
         usage = "(LOL Username)",
         description = "Look up Leauge of Legends statistics " + "of a player.")
public class LeagueLookupCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        note.error("This is being worked on right now, spam Maeyrl to fix me.");

        /*
        StringJoiner joiner = new StringJoiner("");
        try
        {
            JSONObject jso = Utils.jsonFromUrl("https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/" +
                    StringUtils.join(args, "").toLowerCase() + "?api_key=" + Bot.INSTANCE.getAuthTokens().getProperty
                    ("league"));
            JSONObject info = jso.getJSONObject(args[0]);
            joiner.add("**League Of Legends** Account Info: ");
            joiner.add("Season: **SEASON 2016** \n");
            joiner.add("Current Name: **" + info.get("name") + "**");
            joiner.add("ID: **" + info.get("id") + "**");
            joiner.add("Level: **" + info.get("summonerLevel") + "**");
            joiner.add("Icon ID: **" + info.get("profileIconId") + "**");
            
            JSONObject jso2 = Utils.jsonFromUrl("https://na.api.pvp.net/api/lol/na/v1.3/stats/by-summoner/" + info
                    .get("id") + "/summary?season=SEASON2016&api_key=" + Bot.INSTANCE.getAuthTokens().getProperty
                    ("league"));
            JSONArray j = jso2.getJSONArray("playerStatSummaries");
            JSONObject fin = j.getJSONObject(0);
            
            joiner.add("Wins: **" + fin.get("wins") + "**");
            
            JSONObject stats = (JSONObject) fin.get("aggregatedStats");
            
            joiner.add("Champion Kills: **" + stats.get("totalChampionKills") + "**");
            joiner.add("Minion Kills: **" + stats.get("totalMinionKills") + "**");
            joiner.add("Assists: **" + stats.get("totalAssists") + "**");
            joiner.add("Neutral Minion Kills: **" + stats.get("totalNeutralMinionsKilled") + "**");
            joiner.add("Turrets Killed: **" + stats.get("totalTurretsKilled") + "**");
            
            note.getChannel().sendMessage(joiner.toString()).queue();
        }
        catch (Exception e)
        {
            note.getChannel().sendMessage("Account not found or there was an error connecting to the LoL API\n" +
                    "Here is what I was able to get: ");
            try
            {
                note.getChannel().sendMessage(joiner.toString()).queue();
            }
            catch (Exception ignore) {}
        }
        
        MessageBuilder mb = new MessageBuilder();
        try
        {
            JSONObject jso = Utils.jsonFromUrl("https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/" +
                    StringUtils.join(args, "").toLowerCase() + "?api_key=" + Bot.INSTANCE.getAuthTokens().getProperty
                    ("league"));
            JSONObject info = jso.getJSONObject(args[0]);
            joiner.add("**League Of Legends** Account Info: ");
            joiner.add("Season: **SEASON 2016** \n");
            joiner.add("Current Name: **" + info.get("name") + "**");
            joiner.add("ID: **" + info.get("id") + "**");
            joiner.add("Level: **" + info.get("summonerLevel") + "**");
            joiner.add("Icon ID: **" + info.get("profileIconId") + "**");
            
            try
            {
                JSONObject jso2 = Utils.jsonFromUrl("https://na.api.pvp.net/api/lol/na/v1.3/stats/by-summoner/" +
                        info.get("id") + "/summary?season=SEASON2016&api_key=" + Bot.INSTANCE.getAuthTokens()
                        .getProperty("league"));
                JSONArray j = jso2.getJSONArray("playerStatSummaries");
                JSONObject fin = j.getJSONObject(0);
                
                joiner.add("Wins: **" + fin.get("wins") + "**");
                
                JSONObject stats = (JSONObject) fin.get("aggregatedStats");
                
                joiner.add("Champion Kills: **" + stats.get("totalChampionKills") + "**");
                joiner.add("Assists: **" + stats.get("totalAssists") + "**");
                joiner.add("Neutral Minion Kills: **" + stats.get("totalNeutralMinionsKilled") + "**");
                joiner.add("Turrets Killed: **" + stats.get("totalTurretsKilled") + "**");
                
                note.getChannel().sendMessage(mb.build());
            }
            catch (Exception e)
            {
                note.getChannel().sendMessage("Account not found or there was an error connecting to the LoL API\n" +
                        "Here is what I was able to get: ").queue();
                try
                {
                    note.getChannel().sendMessage(mb.build()).queue();
                }
                catch (Exception ex) {e.printStackTrace();}
                //e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            note.getChannel().sendMessage("Account not found or there was an error connecting to the LoL API\n" +
                    "Here is what I was able to get: ").queue();
            try
            {
                note.getChannel().sendMessage(mb.build()).queue();
            }
            catch (Exception ignore) {}
        }*/
    }
}
