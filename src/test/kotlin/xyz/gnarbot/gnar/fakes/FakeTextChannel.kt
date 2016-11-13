package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.core.entities.impl.TextChannelImpl


object FakeTextChannel : TextChannelImpl("000000000000000000", null)
{
    /*override fun sendMessage(msg : String) :
    {
        FakeBot.LOG.info(msg)
        return FakeMessage.create(msg)
    }
    
    override fun sendMessage(msg : Message)
    {
        return sendMessage(msg.content)
    }*/
}