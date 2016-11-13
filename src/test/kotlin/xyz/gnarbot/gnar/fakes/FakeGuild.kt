package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.core.entities.impl.GuildImpl

object FakeGuild : GuildImpl(null, "000000000000000000")
{
    init
    {
        //name = "Fake Guild"
        //region = Region.US_CENTRAL
        //isAvailable = true
        //owner = FakeUser
    }
}