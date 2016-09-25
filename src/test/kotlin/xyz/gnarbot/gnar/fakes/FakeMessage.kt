package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.MessageBuilder
import net.dv8tion.jda.entities.Message
import net.dv8tion.jda.entities.MessageChannel
import net.dv8tion.jda.entities.impl.MessageImpl
import net.dv8tion.jda.events.message.MessageReceivedEvent

object FakeMessage
{
    @JvmStatic
    fun create(string : String) : Message
    {
        val msgBuilder = MessageBuilder()
    
        msgBuilder.appendString(string)
    
        return FakeMessageImpl().apply {
            content = string
            author = FakeUser
        }
    }
    
    @JvmStatic
    fun createEvent(string : String) : MessageReceivedEvent
    {
        return MessageReceivedEvent(FakeJDA, 200, FakeMessage.create(string))
    }
    
    class FakeMessageImpl() : MessageImpl("000000000000000000", FakeJDA)
    {
        override fun getChannel() : MessageChannel
        {
            return FakeTextChannel
        }
    }
}

