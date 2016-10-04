package xyz.gnarbot.gnar.fakes

import net.dv8tion.jda.Region
import net.dv8tion.jda.entities.impl.GuildImpl

object FakeGuild : GuildImpl(FakeJDA, "000000000000000000")
{
    init
    {
        name = "Fake Guild"
        region = Region.US_CENTRAL
        isAvailable = true
        owner = FakeUser
    }
}