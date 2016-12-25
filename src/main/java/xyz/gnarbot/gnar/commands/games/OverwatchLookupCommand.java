package xyz.gnarbot.gnar.commands.games;

import net.dv8tion.jda.core.entities.Message;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.NullableJSON;

import java.awt.*;
import java.util.StringJoiner;

import static xyz.gnarbot.gnar.utils.Utils.jsonFromUrl;

@Command(aliases = {"overwatch", "ow"}, usage = "(Battle Tag)", description = "Look up Overwatch information about a " +
        "player.")
public class OverwatchLookupCommand extends CommandExecutor
{


    public void execute(Note message, String label, String[] args)
    {
        if (args.length == 0)
        {
            message.reply("You need to provide a BattleTag.");
            return;
        }
        
        if (!args[0].matches("[a-zA-Z1-9]+#\\d+"))
        {
            message.reply("You did not enter a valid BattleTag.");
            return;
        }
        
        try
        {
            String region = "us";
            if (args.length > 1){
                if (args[1].equalsIgnoreCase("US") || args[1].equalsIgnoreCase("kr") || args[1].equalsIgnoreCase("eu")){
                    region = args[1].toLowerCase();
                }
            }
            StringJoiner joiner = new StringJoiner("\n");
            Message m = message.reply("Finding that player...");
            JSONObject jso;
            try {
                jso = jsonFromUrl("https://owapi.net/api/v3/u/" + args[0].replaceAll("#", "-") +
                        "/stats").getJSONObject(region);
                if (jso == null) {
                    m.deleteMessage().queue();
                    message.reply("Unable to find Overwatch Player `" + args[0] + "`.");
                    return;
                }
            }catch (JSONException e){
                m.deleteMessage().queue();
                message.reply("Unable to find Overwatch Player `" + args[0] + "` within that region.");
                return;
            }

            String tag = args[0].replaceAll("-", "#");
            joiner.add("Battle Tag: **__[" + tag + "](http://masteroverwatch.com/profile/pc/" + region.toLowerCase() + "/" + tag.replaceAll("#", "-") + ")__**");
            joiner.add("Region: **__[" + region.toUpperCase() + "](http://masteroverwatch.com/leaderboards/pc/" + region.toLowerCase() + "/mode/ranked/category/skillrating)__**");
            
            JSONObject overallStats = new NullableJSON(jso.getJSONObject("stats").toString());
            JSONObject compStats = new NullableJSON(overallStats.getJSONObject("competitive").toString());
            JSONObject compOverallStats = new NullableJSON(compStats.getJSONObject("overall_stats").toString());
            JSONObject compGameStats = new NullableJSON(compStats.getJSONObject("game_stats").toString());
            JSONObject quickPStats = new NullableJSON(overallStats.getJSONObject("quickplay").toString());
            JSONObject quickPOverallStats = new NullableJSON(quickPStats.getJSONObject("overall_stats").toString());
            JSONObject quickPGameStats = new NullableJSON(quickPStats.getJSONObject("game_stats").toString());
            
            joiner.add("\n**__General                                    __**");
            joiner.add("Level: **[" + (quickPOverallStats.getInt("prestige") * 100 + quickPOverallStats.getInt("level")) + "]()**");
            joiner.add("\n**__Quick Play                              __**");
            joiner.add("Win Rate: **[" + quickPOverallStats.getInt("win_rate") + "%]()**");
            joiner.add("Win Count: **[" + (quickPOverallStats.getInt("wins")) +" Games]()**");
            joiner.add("\n**__Competitive                           __**");

            int rank = compOverallStats.getInt("comprank");
            joiner.add("Win Rate: **" + (compOverallStats.getInt("wins") / compOverallStats.getInt("games")) + "%**");
            joiner.add("Win Count: **" + (compOverallStats.getInt("wins")) + "/" + (compOverallStats.getInt("games")) +" Games**");
            joiner.add("Losses: **" + (compOverallStats.getInt("losses")) + "/" + (compOverallStats.getInt("games")) +" Games**");
            Color sideColor = Color.ORANGE;
            joiner.add("Draws: **" + (compOverallStats.getInt("games") - (compOverallStats.getInt("losses") + compOverallStats.getInt("wins"))) +" Games**");
            if (rank < 1500) {
                joiner.add("Comp. Rank: **:coffin: " + rank + "**"); // Bronze
                sideColor = new Color(150, 90, 56);
            }else if (rank >= 1500 && rank < 2000){
                joiner.add("Comp. Rank: **:cd: " + rank + "**"); // Silver
                sideColor = new Color(168, 168, 168);
            }else if (rank >= 2000 && rank < 2500){
                joiner.add("Comp. Rank: **:dvd: " + rank + "**"); // Gold
                sideColor = new Color(201, 137, 16);
            }else if (rank >= 2500 && rank < 3000){
                joiner.add("Comp. Rank: **:crossed_swords:" + rank + "**"); // Platinum
                sideColor = new Color(229, 228, 226);
            }else if (rank >= 3000 && rank < 3500){
                joiner.add("Comp. Rank: **:gem: " + rank + "**"); // Diamond
                sideColor = new Color(185,242,255);
            }else if (rank >= 3500 && rank < 4000){
                joiner.add("Comp. Rank: **:yellow_heart: " + rank + "**"); // Master
                sideColor = new Color(255, 184, 12);
            }else if (rank >= 4000){
                joiner.add("Comp. Rank: **:trident: " + rank + "**"); // Grand Master
                sideColor = new Color(238, 180, 255);
            }
            
            joiner.add("\n**__Overall                                    __**");
            joiner.add("Eliminations: **" + (int) (quickPGameStats.getDouble("eliminations") + compGameStats.getDouble("eliminations")) + "**");
            joiner.add("Medals: **" + (int) (quickPGameStats.getDouble("medals") + compGameStats.getDouble("medals")) + "**");
            joiner.add("Total Damage: **" + (int) (quickPGameStats.getDouble("damage_done") + compGameStats.getDouble("damage_done")) + "**");
            joiner.add("Cards: **" + (int) (quickPGameStats.getDouble("cards") + compGameStats.getDouble("cards")) + "**");
            m.deleteMessage().queue();
            message.replyEmbedRaw("**Overwatch Stats for " + tag + "**", joiner.toString(), sideColor, quickPOverallStats.getString("avatar"));
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
