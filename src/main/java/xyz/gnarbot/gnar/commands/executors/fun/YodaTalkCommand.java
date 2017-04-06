package xyz.gnarbot.gnar.commands.executors.fun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.entities.Message;
import org.apache.commons.lang3.StringUtils;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.handlers.Category;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;

import java.util.List;

@Command(
        aliases = {"yodatalk"},
        usage = "(sentence)",
        description = "Learn to speak like Yoda, you will.",
        category = Category.FUN
)
public class YodaTalkCommand extends CommandExecutor {
    @Override
    public void execute(Message message, List<String> args) {
        if (args.isEmpty()) {
            message.respond().error("At least put something. `:[`").queue();
            return;
        }

        try {
            String query = StringUtils.join(args, "+");

            HttpResponse<String> response = Unirest.get("https://yoda.p.mashape.com/yoda?sentence=" + query)
                    //.queryString("sentence", query)
                    .header("X-Mashape-Key", "dw1mYrC2ssmsh2WkFGHaherCtl48p1wtuHWjsnYbP3Y7q8y6M5")
                    .header("Accept", "text/plain")
                    .asString();

            String result = response.getBody();

            message.respond().embed("Yoda-Speak")
                    .setColor(Constants.COLOR)
                    .setDescription(result)
                    .setThumbnail("https://upload.wikimedia.org/wikipedia/en/9/9b/Yoda_Empire_Strikes_Back.png")
                    .rest().queue();

        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}