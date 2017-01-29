package xyz.gnarbot.gnar.utils;

import kotlin.io.FilesKt;
import xyz.gnarbot.gnar.Bot;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class BotData {
    private static final Random random = new Random();

    private static final List<String> botQuotes = FilesKt.readLines(new File(Bot.getFiles()
            .getData(), "quotes/bot" + ".txt"), StandardCharsets.UTF_8);

    @Deprecated
    public static String randomQuote() {
        return botQuotes.get(random.nextInt(botQuotes.size()));
    }
}
