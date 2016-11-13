package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.core.entities.impl.MemberImpl

object FakeMember : MemberImpl(FakeGuild, FakeUser)
{
    
}