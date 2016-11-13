package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.impl.JDAImpl

object FakeJDA : JDAImpl(AccountType.BOT, null, false, false, false, false)
{
    override fun getTextChannelById(id : String?) : TextChannel = FakeTextChannel
}