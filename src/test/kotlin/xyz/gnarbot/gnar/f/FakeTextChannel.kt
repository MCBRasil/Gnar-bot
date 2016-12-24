package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.impl.TextChannelImpl
import net.dv8tion.jda.core.requests.RestAction


object FakeTextChannel : TextChannelImpl("000000000000000000", FakeGuild)
{
    override fun sendMessage(msg : String) : RestAction<Message>?
    {
        FakeBot.LOG.info(msg)
        return null
    }
}