package xyz.gnarbot.gnar;

public class MainBot
{
    // DO NOT MODIFY THIS
    // USE TESTBOT TO RUN TESTS
    public static void main(String[] args)
    {
        Bot.INSTANCE.initBot(Bot.INSTANCE.getAuthTokens().getProperty("main-bot"), 6);
    }
}
