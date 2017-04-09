package xyz.gnarbot.gnar.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String[] stringSplit(String s, final char delimiter) {
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

        return f.toArray(new String[f.size()]);
    }

    public static String getTimestamp(long milliseconds) {
        long seconds = milliseconds / 1000 % 60;
        long minutes = milliseconds / (1000 * 60) % 60;
        long hours = milliseconds / (1000 * 60 * 60) % 24;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
