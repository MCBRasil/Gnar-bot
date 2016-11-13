package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.core.entities.impl.UserImpl


object FakeUser : UserImpl("000000000000000000", null)
{
    init
    {
        //setUserName("Fake User")
        //avatarId = id
        //discriminator = id
        //privateChannel = FakePrivateChannel
    }
}