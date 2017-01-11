package xyz.gnarbot.gnar.commands.executors.fun;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "meme")
public class GenerateMemeCommand extends CommandExecutor {

    @Override
    public void execute(Note msg, String label, String[] args) {
        try {
            String request, memeName, topText, botText;
            if (args.length < 3) {
                msg.error("Please supply more arguments. Example Usage:\n\n_memes Meme:Spongegar Top:That moment when Bot:Someone scares you");
                return;
            }
            request = StringUtils.join(args, " ");
            String[] arguments = request.split("/(Meme:)|(Top:)|(Bot:)/ig");
            HttpResponse<JsonNode> response = Unirest.get("https://api.imgflip.com/caption_image")
                                                        .queryString("template_id", arguments[1])
                                                        .queryString("username", "GNARBot")
                                                        .queryString("password", Bot.INSTANCE.getAuthTokens().getProperty("imgflippass"))
                                                        .queryString("text0", arguments[2])
                                                        .queryString("text1", arguments[3]).asJson();
            JSONObject j = response.getBody().getObject().getJSONObject("data");
            msg.replyRaw(j.get("url").toString());
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

}
