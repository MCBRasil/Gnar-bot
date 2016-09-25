package xyz.gnarbot.gnar.tests

import org.junit.Test
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.Shard
import xyz.gnarbot.gnar.fakes.FakeGuild
import xyz.gnarbot.gnar.fakes.FakeJDA
import xyz.gnarbot.gnar.fakes.FakeMessage
import xyz.gnarbot.gnar.fakes.FakeUser
import kotlin.test.assertEquals

class KotlinTests
{
    private val shard = Shard(0, FakeJDA)
    private val host = shard.getHost(FakeGuild)
    
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
        sendToHost("_wow")
    }
    
    fun sendToHost(string : String)
    {
        host.handleMessageEvent(FakeMessage.createEvent(string))
    }
}