package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.core.utils.SimpleLog
import xyz.gnarbot.gnar.handlers.servers.Shard

object FakeBot
{
    @JvmStatic val LOG = SimpleLog.getLog("Fake-Bot")!!
    @JvmStatic val PLOG = SimpleLog.getLog("Fake-Bot | Private")!!
    
    private val shard = Shard(0, null)
    private val host = shard.getHost(null)
    
    fun send(string : String)
    {
        //host.handleMessageEvent(FakeMessage.createEvent(string))
    }
    
    
}