package xyz.gnarbot.gnar;

/**
 * Main bot instantiation class.
 *
 * To run a test instance, use TestBot.java found in
 * test/java/xyz/gnarbot/gnar/tests folder.
 *
 * Do not modify.
 *
 * @author Avarel, Maeyrl
 */
public class MainBot
{
    public static void main(String[] args)
    {
        Bot.INSTANCE.start(Bot.INSTANCE.getAuthTokens().getProperty("main-bot"), 18);
    }
}
