import xyz.gnarbot.gnar.Bot;

/**
 * Test bot instantiation class.
 *
 * @author Avarel
 */
public class TestBot
{
    public static void main(String[] args)
    {
        Bot.INSTANCE.start(Bot.INSTANCE.getAuthTokens().getProperty("test-bot"), 1);
    }
}
