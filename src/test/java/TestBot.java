import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.Credentials;

/**
 * Test bot instantiation class.
 *
 * @author Avarel
 */
public class TestBot {
    public static void main(String[] args) {
        Bot.INSTANCE.start(Credentials.BETA, 1);
    }
}
