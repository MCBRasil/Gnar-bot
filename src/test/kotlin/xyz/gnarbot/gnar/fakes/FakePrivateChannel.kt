package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.core.entities.impl.PrivateChannelImpl


object FakePrivateChannel : PrivateChannelImpl("000000000000000000", FakeUser)
{
    /* override fun sendMessage(msg : String) : Message
    {
        FakeBot.PLOG.info(msg)
        return FakeMessage.create(msg)
    }
    
    override fun sendMessage(msg : Message) : Message
    {
        return sendMessage(msg.content)
    } */
}