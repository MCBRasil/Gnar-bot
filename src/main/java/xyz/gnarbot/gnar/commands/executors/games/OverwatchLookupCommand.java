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
            
            
            JSONObject overall = jso.optJSONObject("stats");
            
            if (overall == null)
            {
                note.error("Unable to find statistics for Overwatch player`" + tag + "`.");
                return;
            }
            
            String avatar = null;
            int eliminations = 0;
            int medals = 0;
            int dmg_done = 0;
            int cards = 0;
            
            JSONObject qp_stats = overall.optJSONObject("quickplay");
            
            if (qp_stats != null)
            {
                JSONObject qp_overall = qp_stats.optJSONObject("overall_stats");
                JSONObject qp_game = qp_stats.optJSONObject("game_stats");
                JSONObject qp_avg = qp_stats.optJSONObject("average_stats");
                
                
                if (qp_overall != null)
                {
                    joiner.add("\n**__General                                                      __**");
                    joiner.add("  Level: **[" + (qp_overall.optInt("prestige") * 100 + qp_overall.optInt("level")) + "]()**");
                    
                    avatar = qp_overall.optString("avatar");
                }
                
                joiner.add("\n**__Quick Play                                                 __**");
                
                if (qp_avg != null)
                {
                    joiner.add("  Avg. Elims: **[" + qp_avg.optDouble("eliminations_avg") + "]()**");
                    joiner.add("  Avg. Deaths: **[" + qp_avg.optDouble("deaths_avg") + "]()**");
                    joiner.add("  Avg. Final Blows: **[" + qp_avg.optDouble("final_blows_avg") + "]()**");
                }
                
                if (qp_overall != null)
                {
                    joiner.add("  Wins: **[" + (qp_overall.optInt("wins")) + "]()**");
                }
                if (qp_game != null)
                {
                    joiner.add("  K/D Ratio: **[" + (qp_game.optDouble("kpd")) + "]()**");
                    joiner.add("  Played for: **[" + (qp_game.optInt("time_played")) + " hours]()**");
    
                    eliminations += qp_game.optDouble("eliminations");
                    medals += qp_game.optDouble("medals");
                    dmg_done += qp_game.optDouble("damage_done");
                    cards += qp_game.optDouble("cards");
                }
            }
            
            JSONObject cp_stats = overall.optJSONObject("competitive");
            Color sideColor = null;
            String rankName = null;
            
            if (cp_stats != null)
            {
                JSONObject cp_overall = cp_stats.optJSONObject("overall_stats");
                JSONObject cp_game = cp_stats.optJSONObject("game_stats");
                JSONObject cp_avg = cp_stats.optJSONObject("average_stats");
                
                joiner.add("\n**__Competitive                                              __**");
                
                if (cp_avg != null)
                {
                    joiner.add("  Avg. Elims: **[" + cp_avg.optDouble("eliminations_avg") + "]()**");
                    joiner.add("  Avg. Deaths: **[" + cp_avg.optDouble("deaths_avg") + "]()**");
                }
    
                if (cp_game != null)
                {
                    joiner.add("  Wins/Draws/Loses: **["
                            + cp_game.optInt("games_won") + "]()** | **["
                            + cp_game.optInt("games_tied") + "]()** | **["
                            + cp_game.optInt("games_lost") + "]()**");
                    joiner.add("  K/D Ratio: **[" + cp_game.optDouble("kpd") + "]()**");
                    joiner.add("  Played for: **[" + cp_game.optInt("time_played") + " hours]()**");
                    
                    eliminations += cp_game.optDouble("eliminations");
                    medals += cp_game.optDouble("medals");
                    dmg_done += cp_game.optDouble("damage_done");
                    cards += cp_game.optDouble("cards");
                }
                if (cp_overall != null)
                {
                    int rank = cp_overall.optInt("comprank");
    
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
                }
            }
            
            joiner.add("\n**__Overall                                                        __**");
            joiner.add("  Eliminations: **[" + eliminations + "]()**");
            joiner.add("  Medals: **[" + medals + "]()**");
            joiner.add("  Total Damage: **[" + dmg_done + "]()**");
            joiner.add("  Cards: **[" + cards + "]()**");
            
            note.replyEmbedRaw("**Overwatch Stats: " + tag + "**", joiner.toString(), sideColor, avatar);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
