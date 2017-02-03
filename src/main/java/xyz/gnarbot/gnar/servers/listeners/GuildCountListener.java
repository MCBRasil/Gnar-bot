package xyz.gnarbot.gnar.servers.listeners;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.servers.Shard;

public class GuildCountListener extends ListenerAdapter {
    public static GuildCountListener INSTANCE = new GuildCountListener();

    private int changes = 0;

    private GuildCountListener() {}

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        updateQueue();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        updateQueue();
    }

    private void updateQueue() {
        changes++;

        if (changes > 20) {
            update();
            changes = 0;
        }
    }

    /**
     * Updates Server Counts on ad sites
     */
    public void update() {
        int count = 0;

        for (Shard s : Bot.INSTANCE.getShards()) {
            count += s.getJDA().getGuilds().size();
        }

        updateServerCount(count);
    }

    public void updateServerCount(int i) {
        updateAbalCount(i);
        updateCarbonitexCount(i);
    }

    public void updateAbalCount(int i) {
        try {
            String auth = Bot.INSTANCE.getAuthTokens().getProperty("authToken");

            JSONObject json = new JSONObject().put("server_count", i);

            String response = Unirest.post(Bot.INSTANCE.getAuthTokens().getProperty("url"))
                    .header("User-Agent", "Gnar Bot")
                    .header("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(json)
                    .asString()
                    .getStatusText();

            Bot.getLOG().info("Successfully updated Abal server count to " + i + ".");
            Bot.getLOG().info("Response code: " + response);
        } catch (UnirestException e) {
            Bot.getLOG().warn("Failed updating Abal server count to " + i + ".");
            e.printStackTrace();
        }
    }

    public void updateCarbonitexCount(int i) {
        try {
            String auth = Bot.INSTANCE.getAuthTokens().getProperty("authToken");
            String key = Bot.INSTANCE.getAuthTokens().getProperty("serverKey");

            JSONObject json = new JSONObject().put("key", key).put("servercount", i);

            String response = Unirest.post("https://www.carbonitex.net/discord/data/botdata.php")
                    .header("User-Agent", "Gnar Bot")
                    .header("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(json)
                    .asString()
                    .getStatusText();

            Bot.getLOG().info("Successfully updated Carbonitex server count to " + i + ".");
            Bot.getLOG().info("Response code: " + response);
        } catch (UnirestException e) {
            Bot.getLOG().warn("Failed updating Carbonitex server count to " + i + ".");
            e.printStackTrace();
        }
    }
}
