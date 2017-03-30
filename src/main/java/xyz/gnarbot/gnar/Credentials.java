package xyz.gnarbot.gnar;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Credentials {
    public final static String PRODUCTION;
    public final static String BETA;

    public final static String MARVEL_PU;
    public final static String MARVEL_PR;

    public final static String ABAL_URL;
    public final static String ABAL_TOKEN;

    public final static String LEAUGE;
    public final static String IMGFLIP;

    public final static String CARBONITEX;

    public final static String MASHAPE;

    static {
        JSONObject jso = null;
        try {
            File file = new File(new File("data"), "credentials.json");
            jso = new JSONObject(new JSONTokener(new FileReader(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert jso != null;
        JSONObject token = jso.getJSONObject("token");
        PRODUCTION = token.optString("production");
        BETA = token.optString("beta");
        JSONObject marvel = jso.optJSONObject("marvel");
        MARVEL_PU = marvel.optString("pu");
        MARVEL_PR = marvel.optString("pr");
        JSONObject abal = jso.optJSONObject("abal");
        ABAL_URL = abal.optString("url");
        ABAL_TOKEN = abal.optString("token");
        LEAUGE = jso.optString("leauge");
        IMGFLIP = jso.optString("imgflip");
        CARBONITEX = jso.optString("carbonitex");
        MASHAPE = jso.optString("mashape");
    }

    private Credentials() {}
}
