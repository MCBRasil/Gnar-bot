package xyz.gnarbot.gnar.commands.executors.fun;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

@Command(aliases = "meme")
public class GenerateMemeCommand extends CommandExecutor {

    @Override
    public void execute(Note msg, String label, String[] args) {
        try {
            JSONObject memeInfo = Utils.memes.getJSONObject("data");
            JSONArray memeList = memeInfo.getJSONArray("memes");
            if(args[0].equalsIgnoreCase("list")) {
                int page = 1;
                try {
                    if (args[1] != null) {
                        page = Integer.valueOf(args[1]);
                    }
                } catch (Exception e) {}
                if (page == 0) { page = 1; }
                ArrayList<String> memeNames = new ArrayList<>();

                for(int i=0; i<memeList.length(); i++) {
                    memeNames.add(memeList.getJSONObject(i).getString("name"));
                }

                Collections.sort(memeNames);

                String mb = "";

                int pages;

                if(memeNames.size() % 10 == 0) { pages = memeNames.size()/10;}
                else { pages = memeNames.size()/10+1; }

                if(page > pages) page = pages;

                int i = 0;
                for (String g : memeNames) {
                    i++;
                    if (i < 10 * page + 1&& i > 10 * page - 10) {
                        mb = mb + "**#" + i + "** [" + g + "]()\n";
                    }
                }

                msg.replyEmbed(("Meme List (Page "+ page + "/" + pages + ")"), mb, Bot.getColor());
                return;
            }


            String request = StringUtils.join(args, " ");
            String[] arguments = request.split("\\|");
            int distance = 999;
            JSONObject meme = null;
            for(int i=0; i<memeList.length(); i++) {
                JSONObject jTemp = memeList.getJSONObject(i);
                int tempDistance = StringUtils.getLevenshteinDistance(jTemp.getString("name"), arguments[0].trim());
                if(tempDistance < distance) {
                    distance = tempDistance;
                    meme = jTemp;
                }
            }
            HttpResponse<JsonNode> response = Unirest.get("https://api.imgflip.com/caption_image")
                                                        .queryString("template_id", meme.getString("id").toString())
                                                        .queryString("username", "GNARBot")
                                                        .queryString("password", Bot.INSTANCE.getAuthTokens().getProperty("imgflippass"))
                                                        .queryString("text0", arguments[1].trim())
                                                        .queryString("text1", arguments[2].trim()).asJson();
            JSONObject j = response.getBody().getObject().getJSONObject("data");
            msg.replyRaw(j.get("url").toString());
        } catch (Exception e) {
            msg.error("**Please supply more arguments. Example Usage:**\n\n[_meme Spongegar | Top Text | Bottom Text]()\n\n**For a list of memes, type:**\n\n[_meme list (page #)]()"); }
    }

}
