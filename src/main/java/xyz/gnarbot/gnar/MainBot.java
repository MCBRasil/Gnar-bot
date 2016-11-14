package xyz.gnarbot.gnar;

public class MainBot
{
    public static void main(String[] args)
    {
        Bot.INSTANCE.initBot(Bot.INSTANCE.getAuthTokens().getProperty("main-bot"), 4);
    }
}
