package xyz.gnarbot.gnar.tests

import org.junit.Test
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.fakes.FakeBot
import xyz.gnarbot.gnar.fakes.FakeMessage
import xyz.gnarbot.gnar.fakes.FakeUser
import kotlin.test.assertEquals

class KotlinTests
{
    @Test
    fun `Initialize the bot`()
    {
        Bot.initBot(Bot.authTokens.getProperty("test-bot"), 1)
    }
    
    @Test
    fun `Test fake JDA objects`()
    {
        assertEquals(FakeUser.id, "000000000000000000")
        FakeMessage.create("?").channel.sendMessage("Hey there.")
    }
    
    @Test
    fun `TestCommand test`()
    {
        FakeBot.sendToHost("_wow")
    }
    
    @Test
    fun `HelpCommand test`()
    {
        FakeBot.sendToHost("_help")
    }
    
    @Test
    fun `OverwatchLookupCommand test`()
    {
        FakeBot.sendToHost("_ow Avalon#11557")
    }
}