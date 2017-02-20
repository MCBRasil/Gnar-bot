package xyz.gnarbot.gnar.tests;

import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.junit.Test;
import xyz.gnarbot.gnar.Credentials;

public class UnirestPosting {
    @Test
    public void unirestUpdate() throws Exception {
        int i = 7000;

        String auth = Credentials.ABAL_TOKEN;
        String key = Credentials.CARBONITEX;

        JSONObject json = new JSONObject()
                .put("key", key)
                .put("servercount", i);

        String response = Unirest.post("https://www.carbonitex.net/discord/data/botdata.php")
                .header("User-Agent", "Gnar Bot")
                .header("Authorization", auth)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(json)
                .asString().getStatusText();

        System.out.println(response);
    }

    @Test
    public void unirestUpdate2() throws Exception {
        int i = 7000;

        String auth = Credentials.ABAL_TOKEN;

        JSONObject json = new JSONObject()
                .put("server_count", i);

        String response = Unirest.post(Credentials.ABAL_URL)
                .header("User-Agent", "Gnar Bot")
                .header("Authorization", auth)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(json)
                .asString().getStatusText();

        System.out.println(response);
    }
}
