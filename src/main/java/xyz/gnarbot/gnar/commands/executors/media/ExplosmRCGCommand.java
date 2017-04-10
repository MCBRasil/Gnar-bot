package xyz.gnarbot.gnar.commands.executors.media;

import net.dv8tion.jda.core.entities.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import xyz.gnarbot.gnar.Constants;
import xyz.gnarbot.gnar.commands.Category;
import xyz.gnarbot.gnar.commands.Command;
import xyz.gnarbot.gnar.commands.CommandExecutor;

@Command(
        aliases = "rcg",
        description = "Generate random Cyanide and Happiness comic.",
        category = Category.FUN
)
public class ExplosmRCGCommand extends CommandExecutor {
    @Override
    public void execute(Message message, String[] args) {
        try {
            Document document;

            document = Jsoup.connect("http://explosm.net/rcg").get();

            Element element = document.getElementById("rcg-comic").getElementsByTag("img").first();

            String url = element.absUrl("src");

            String logo = "http://explosm.net/img/logo.png";

            message.respond().embed("Cyanide and Happiness")
                    .setColor(Constants.COLOR)
                    .setDescription("**Random Comic Generator**")
                    .setImage(url)
                    .setThumbnail(logo)
                    .rest().queue();

        } catch (Exception e) {
            message.respond().error("Unable to grab random Cyanide and Happiness comic.").queue();
            e.printStackTrace();
        }
    }
}

