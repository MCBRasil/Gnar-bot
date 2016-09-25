package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.entities.Message
import net.dv8tion.jda.entities.impl.TextChannelImpl
import xyz.gnarbot.gnar.Bot

object FakeTextChannel : TextChannelImpl("000000000000000000", FakeGuild)
{
    override fun sendMessage(msg : String) : Message
    {
        Bot.LOG.info(msg)
        return FakeMessage.create(msg)
    }
}