package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.impl.PrivateChannelImpl
import net.dv8tion.jda.core.requests.RestAction


object FakePrivateChannel : PrivateChannelImpl("000000000000000000", FakeUser)
{
    override fun sendMessage(msg : String) : RestAction<Message>?
    {
        FakeBot.PLOG.info(msg)
        return null
    }
}