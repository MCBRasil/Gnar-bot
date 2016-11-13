package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.impl.MessageImpl
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import kotlin.jvm.JvmStatic as static

object FakeMessage
{
    @static fun create(string : String) : Message
    {
        val msgBuilder = MessageBuilder()

        msgBuilder.appendString(string)

        return FakeMessageImpl().apply {
            content = string
            author = FakeUser
            
        }
    }

    @static fun createEvent(string : String) : MessageReceivedEvent
    {
        return object : MessageReceivedEvent(FakeJDA, 200, FakeMessage.create(string))
        {
            override fun getMember() : Member
            {
                return FakeMember
            }
        }
    }

    class FakeMessageImpl() : MessageImpl("000000000000000000", FakeTextChannel, false)
}

