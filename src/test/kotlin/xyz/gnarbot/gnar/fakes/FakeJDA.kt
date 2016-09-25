package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.entities.TextChannel
import net.dv8tion.jda.entities.impl.JDAImpl

object FakeJDA : JDAImpl(false, false, false)
{
    override fun getTextChannelById(id : String?) : TextChannel
    {
        return FakeTextChannel
    }
}