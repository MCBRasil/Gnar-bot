package xyz.gnarbot.gnar.commands.games

import com.mashape.unirest.http.Unirest
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import java.util.StringJoiner

@Command(aliases = arrayOf("overwatch", "ow"), usage = "(us/eu/kr) (Battle Tag)", description = "Look up Overwatch information about a player.")
class OverwatchLookupCommand : CommandExecutor()
{
    override fun execute(message : Note, label : String, args : Array<String>)
    {
        when (args.size)
        {
            0 ->
            {
                message.reply("You need to provide a region. `[us, kr, eu]`")
                return
            }
            1 ->
            {
                message.reply("You need to provide a BattleTag.")
                return
            }
        }
    
        val region = args[0].toLowerCase()
    
        if (region != "us" && region != "eu" && region != "kr")
        {
            message.reply("Invalid region. `[us, kr, eu]`")
            return
        }
        
        if (!args[1].matches("""[a-zA-Z1-9]+#\d+""".toRegex()))
        {
            message.reply("You did not enter a valid BattleTag in the form of `[a-zA-Z1-9]#0000`.")
            return
        }
        
        val tag = args[1].replace("#".toRegex(), "-")
    
        try
        {
            val joiner = StringJoiner("\n")
        
            val response = Unirest.get("https://owapi.net/api/v3/u/$tag/stats")
                    .asJson().body.`object`
        
            if (response == null)
            {
                message.reply("Unable to find Overwatch Player `" + args[1] + "`.")
                return
            }
    
            joiner.add("Battle Tag: [**$tag**](https://playoverwatch.com/en-gb/career/pc/$region/$tag)")
            joiner.add("Region: **${region.toUpperCase()}**")
        
            val regionJ = response.getJSONObject(region)
            
            val stats = regionJ.getJSONObject("stats")
    
            
            val quick = stats.getJSONObject("quickplay")
            val quick_avg = quick.getJSONObject("average_stats")
            val quick_overall = quick.getJSONObject("overall_stats")
            val quick_game = quick.getJSONObject("game_stats")
            
            val comp = stats.getJSONObject("competitive")
            val comp_avg = comp.getJSONObject("average_stats")
            val comp_overall = comp.getJSONObject("overall_stats")
            val comp_game = comp.getJSONObject("game_stats")
            
            val icon = quick_overall.getString("avatar")
    
            
            joiner.add("Level: **${(quick_overall.getInt("prestige") * 100 + quick_overall.getInt("level"))}**")
            
            
            
            
            joiner.add("\n**__Quick Play                                 __**")
            joiner.add("  Avg. Elims: **${quick_avg.getDouble("eliminations_avg")}**")
            joiner.add("  Avg. Deaths: **${quick_avg.getDouble("deaths_avg")}**")
//            joiner.add("  Damage Done: **${quick_avg.getDouble("damage_done_avg")}**")
//            joiner.add("  Healing Done: **${quick_avg.getDouble("healing_done_avg")}**")
//            joiner.add("  Objective Kills: **${quick_avg.getDouble("objective_kills_avg")}**")
            joiner.add("  Wins: **${quick_game.getInt("games_won")}**")
            joiner.add("  K/D Ratio: **${quick_game.getDouble("kpd")}**")
//            joiner.add("  Medals: **${quick_game.getInt("medals")}**")
//            joiner.add("  Cards: **${quick_game.getInt("cards")}**")
            joiner.add("  Played for: **${quick_game.getInt("time_played")} hours**")
            
            joiner.add("\n**__Competitive                               __**")
            joiner.add("  Avg. Elims: **${comp_avg.getDouble("eliminations_avg")}**")
            joiner.add("  Avg. Deaths: **${comp_avg.getDouble("deaths_avg")}**")
//            joiner.add("  Damage Done: **${comp_avg.getDouble("damage_done_avg")}**")
//            joiner.add("  Healing Done: **${comp_avg.getDouble("healing_done_avg")}**")
//            joiner.add("  Objective Kills: **${comp_avg.getDouble("objective_kills_avg")}**")
            joiner.add("  Skill Rating: \uD83D\uDD30 **${comp_overall.get("comprank")}**")
            joiner.add("  Wins/Draws/Loses: **${comp_game.getInt("games_won")}** | **${comp_game.getInt("games_tied")}** | **${comp_game.getInt("games_lost")}**")
            joiner.add("  K/D Ratio: **${comp_game.getDouble("kpd")}**")
//            joiner.add("  Medals: **${comp_game.getInt("medals")}**")
//            joiner.add("  Cards: **${comp_game.getInt("cards")}**")
            joiner.add("  Played for: **${comp_game.getInt("time_played")} hours**")
            
            message.replyEmbed("Overwatch Stats", joiner.toString(), Bot.color, icon)
        
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    
    }
}
