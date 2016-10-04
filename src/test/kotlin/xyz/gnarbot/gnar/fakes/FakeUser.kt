package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.entities.impl.UserImpl

object FakeUser : UserImpl("000000000000000000", FakeJDA)
{
    init
    {
        setUserName("Fake User")
        avatarId = id
        discriminator = id
        privateChannel = FakePrivateChannel
    }
}