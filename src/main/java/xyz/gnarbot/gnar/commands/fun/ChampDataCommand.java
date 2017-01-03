package xyz.gnarbot.gnar.commands.fun;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.handlers.commands.Command;
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

@Command(aliases = "champdata")
public class ChampDataCommand extends CommandExecutor
{
    public static String[] names = {"Aatrox", "Ahri", "Akali", "Alistar", "Amumu", "Anivia", "Annie", "Ashe",
            "AurelionSol", "Azir", "Bard", "Blitzcrank", "Brand", "Braum", "Caitlyn", "Cassiopeia", "Chogath",
            "Corki", "Darius", "Diana", "DrMundo", "Draven", "Ekko", "Elise", "Evelynn", "Ezreal", "Fiddlesticks",
            "Fiora", "Fizz", "Galio", "Gangplank", "Garen", "Gnar", "Gragas", "Graves", "Hecarim", "Heimerdinger",
            "Illaoi", "Irelia", "Janna", "JarvanIV", "Jax", "Jayce", "Jhin", "Jinx", "Kalista", "Karma", "Karthus",
            "Kassadin", "Katarina", "Kayle", "Kennen", "Khazix", "Kindred", "Kled", "Kogmaw", "LeBlanc", "LeeSin",
            "Leona", "Lissandra", "Lucian", "Lulu", "Lux", "Malphite", "Malzahar", "Maokai", "MasterYi",
            "MissFortune", "Mordekaiser", "Morgana", "Nami", "Nasus", "Nautilus", "Nidalee", "Nocturne", "Nunu",
            "Olaf", "Orianna", "Pantheon", "Poppy", "Quinn", "Rammus", "Reksai", "Renekton", "Rengar", "Riven",
            "Rumble", "Ryze", "Sejuani", "Shaco", "Shen", "Shyvana", "Singed", "Sion", "Sivir", "Skarner", "Sona",
            "Soraka", "Swain", "Syndra", "TahmKench", "Taliyah", "Talon", "Taric", "Teemo", "Thresh", "Tristana",
            "Trundle", "Tryndamere", "TwistedFate", "Twitch", "Udyr", "Urgot", "Varus", "Vayne", "Veigar", "Velkoz",
            "Vi", "Viktor", "Vladimir", "Volibear", "Warwick", "Wukong", "Xerath", "Xin", "Zhao", "Yasuo", "Yorick",
            "Zac", "Zed", "Ziggs", "Zilean", "Zyra"};
    
    @Inject
    public Host host;
    
    @Override
    public void execute(Note note, String label, String[] args)
    {
        int maybeDistance = 20;
        String maybe = "";
        
        for (String s : names)
        {
            int distance = StringUtils.getLevenshteinDistance(s, StringUtils.join(args, "").replaceAll("'", ""));
            if (maybeDistance > distance)
            {
                maybeDistance = distance;
                maybe = s;
            }
        }
        
        JSONObject jso = (JSONObject) Utils.information.get(maybe);
        
        JSONArray spells = jso.getJSONArray("spells");
        
        
        String spellInfo = "**" + maybe + "**: " + jso.get("title") + "\n";
        String key = "";
        
        
        for (int i = 0; i < spells.length(); i++)
        {
            JSONObject spellOne = (JSONObject) spells.get(i);
            
            switch (i)
            {
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
        for (int i = 0; i < skins.length(); i++)
        {
            JSONObject j = (JSONObject) skins.get(i);
            
            int fuckTits = i + 1;
            
            spellInfo += "\n    **" + fuckTits + "**: " + j.get("name");
        }
        
        note.replyRaw(spellInfo);
    }
    
}
