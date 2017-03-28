package xyz.gnarbot.gnar.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
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
