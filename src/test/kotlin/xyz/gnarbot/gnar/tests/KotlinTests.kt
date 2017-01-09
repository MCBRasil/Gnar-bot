package xyz.gnarbot.gnar.tests

import org.junit.Test
import xyz.gnarbot.gnar.Bot

class KotlinTests
{
    @Test
    fun `Initialize the bot`()
    {
        Bot.start(Bot.authTokens.getProperty("test-bot"), 1)
    }
    
    @Test
    fun `Test fake JDA objects`()
    {
        //assertEquals(FakeUser.id, "000000000000000000")
        //FakeMessage.create("?").channel.sendMessage("Hey there.")
    }
}