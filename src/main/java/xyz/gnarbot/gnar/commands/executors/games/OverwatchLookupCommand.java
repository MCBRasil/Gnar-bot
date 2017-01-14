package xyz.gnarbot.gnar.commands.executors.games;

import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

import java.awt.*;
import java.util.StringJoiner;

@Command(aliases = {"overwatch", "ow"},
         usage = "-BattleTag#0000 ~region",
         description = "Look up Overwatch information about a player.")
public class OverwatchLookupCommand extends CommandExecutor
{
    String[] regions = {"us", "eu", "kr"};
    
    public void execute(Note note, String label, String[] args)
    {
        if (args.length == 0)
        {
            note.error("Insufficient arguments. `" + getUsage() + "`.");
            return;
        }
        
        if (!args[0].matches("[a-zA-Z1-9]+(#|-)\\d+"))
        {
            note.error("You did not enter a valid BattleTag `[BattleTag#0000]`.");
            return;
        }
        
        try
        {
            String tag = args[0].replaceAll("#", "-");
            String region = null;
    
            if (args.length > 1)
            {
                for (String r : regions)
                {
                    if (args[1].equalsIgnoreCase(r))
                    {
                        region = r;
                    }
                }
                if (region == null)
                {
                    note.error("Invalid region provided. `[us, eu, kr]`");
                    return;
                }
            }
            
            StringJoiner joiner = new StringJoiner("\n");
            
            JSONObject response = Unirest.get("https://owapi.net/api/v3/u/{tag}/stats")
                    .routeParam("tag", tag)
                    .asJson()
                    .getBody()
                    .getObject();
            
            JSONObject jso = null;
            
            // Region arg provided.
            if (region != null)
            {
                jso = response.optJSONObject(region);
    
                if (jso == null)
                {
                    note.error("Unable to find Overwatch player `" + tag + "` in region `" + region.toUpperCase() + "`.");
                    return;
                }
            }
            // Region arg not provided. Search for first non-null region.
            else
            {
                for (String r : regions)
                {
                    jso = response.optJSONObject(r);
                    
                    if (jso != null)
                    {
                        region = r;
                        break;
                    }
                }
    
                if (jso == null)
                {
                    note.error("Unable to find Overwatch player `" + tag + "`.");
                    return;
                }
            }
            
            
            joiner.add("Battle Tag: **__[" + tag + "](https://playoverwatch.com/en-gb/career/pc/" + region + "/" + tag + ")__**");
            
            joiner.add("Region: **__[" + region.toUpperCase() + "](http://masteroverwatch.com/leaderboards/pc/" + region + "/mode/ranked/category/skillrating)__**");
            
            
            JSONObject overallStats = jso.optJSONObject("stats");
            
            if (overallStats == null)
            {
                note.error("Unable to find statistics for Overwatch player`" + tag + "`.");
                return;
            }
            
            JSONObject compStats = overallStats.getJSONObject("competitive");
            JSONObject compOverallStats = compStats.getJSONObject("overall_stats");
            JSONObject compGameStats = compStats.getJSONObject("game_stats");
            JSONObject compAvgStats = compStats.getJSONObject("average_stats");
            JSONObject quickPStats = overallStats.getJSONObject("quickplay");
            JSONObject quickPOverallStats = quickPStats.getJSONObject("overall_stats");
            JSONObject quickPGameStats = quickPStats.getJSONObject("game_stats");
            JSONObject quickPAvgStats = quickPStats.getJSONObject("average_stats");
            
            joiner.add("\n**__General                                                      __**");
            joiner.add("  Level: **["
                    + (quickPOverallStats.optInt("prestige") * 100
                    + quickPOverallStats.optInt("level")) + "]()**");
            joiner.add("\n**__Quick Play                                                 __**");
            joiner.add("  Avg. Elims: **[" + quickPAvgStats.optDouble("eliminations_avg") + "]()**");
            joiner.add("  Avg. Deaths: **[" + quickPAvgStats.optDouble("deaths_avg") + "]()**");
            joiner.add("  Avg. Final Blows: **[" + quickPAvgStats.optDouble("final_blows_avg") + "]()**");
            joiner.add("  Wins: **[" + (quickPOverallStats.optInt("wins")) + "]()**");
            joiner.add("  K/D Ratio: **[" + (quickPGameStats.optDouble("kpd")) + "]()**");
            joiner.add("  Played for: **[" + (quickPGameStats.optInt("time_played")) + " hours]()**");
            
            joiner.add("\n**__Competitive                                              __**");
            
            int rank = compOverallStats.optInt("comprank");
            joiner.add("  Avg. Elims: **[" + compAvgStats.optDouble("eliminations_avg") + "]()**");
            joiner.add("  Avg. Deaths: **[" + compAvgStats.optDouble("deaths_avg") + "]()**");
            joiner.add("  Wins/Draws/Loses: **[" + compGameStats.optInt("games_won") + "]()** | **["
                    + compGameStats.optInt("games_tied") + "]()** | **["
                    + compGameStats.optInt("games_lost") + "]()**");
            joiner.add("  K/D Ratio: **[" + compGameStats.optDouble("kpd") + "]()**");
            joiner.add("  Played for: **[" + compGameStats.optInt("time_played") + " hours]()**");
            
            Color sideColor = null;
            String rankName = null;
            if (rank < 1500)
            {
                sideColor = new Color(150, 90, 56);
                rankName = "Bronze";
            }
            else if (rank >= 1500 && rank < 2000)
            {
                sideColor = new Color(168, 168, 168);
                rankName = "Silver";
            }
            else if (rank >= 2000 && rank < 2500)
            {
                sideColor = new Color(201, 137, 16);
                rankName = "Gold";
            }
            else if (rank >= 2500 && rank < 3000)
            {
                sideColor = new Color(229, 228, 226);
                rankName = "Platinum";
            }
            else if (rank >= 3000 && rank < 3500)
            {
                sideColor = new Color(63, 125, 255);
                rankName = "Diamond";
            }
            else if (rank >= 3500 && rank < 4000)
            {
                sideColor = new Color(255, 184, 12);
                rankName = "Master";
            }
            else if (rank >= 4000)
            {
                sideColor = new Color(238, 180, 255);
                rankName = "Grand Master";
            }
            
            joiner.add("  Comp. Rank: **[:beginner: " + rank + "]() (" + rankName + ")**");
            
            joiner.add("\n**__Overall                                                        __**");
            joiner.add("  Eliminations: **[" + (int) (quickPGameStats.optDouble("eliminations") + compGameStats
                    .optDouble("eliminations")) + "]()**");
            joiner.add("  Medals: **[" + (int) (quickPGameStats.optDouble("medals") + compGameStats.optDouble
                    ("medals")) + "]()**");
            joiner.add("  Total Damage: **[" + (int) (quickPGameStats.optDouble("damage_done") + compGameStats
                    .optDouble("damage_done")) + "]()**");
            joiner.add("  Cards: **[" + (int) (quickPGameStats.optDouble("cards") + compGameStats.optDouble("cards"))
                    + "]()**");
            
            //m.deleteMessage().queue();
            
            note.replyEmbedRaw("**Overwatch Stats: " + tag + "**", joiner.toString(), sideColor, quickPOverallStats
                    .getString("avatar"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
