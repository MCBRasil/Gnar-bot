package xyz.gnarbot.gnar.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static JSONObject information;

    public static void setLeagueInfo() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("_DATA/league/League.txt")));

            String info = "";

            String line;
            while ((line = br.readLine()) != null) {
                info += line;
            }

            information = new JSONObject(info);
        } catch (Exception ignore) { }
    }

    public static List<String> fastSplit(String s, char delimiter) {
        List<String> f = new ArrayList<>();

        int p = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == delimiter) {
                f.add(s.substring(p, i));
                p = i + 1;

                while (i < s.length() - 1 && s.charAt(i + 1) == delimiter) {
                    i++;
                    p++;
                }
            }
        }
        f.add(s.substring(p));

        return f;
    }
}
