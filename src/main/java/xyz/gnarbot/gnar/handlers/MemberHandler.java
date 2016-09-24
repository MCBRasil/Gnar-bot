package xyz.gnarbot.gnar.handlers;

import net.dv8tion.jda.entities.User;
import xyz.gnarbot.gnar.GuildHandler;

import java.util.HashMap;
import java.util.Map;

public class MemberHandler
{
    private final GuildHandler guildHandler;
    private final Map<User, Member> registry = new HashMap<>();
    
    public MemberHandler(GuildHandler guildHandler)
    {
        this.guildHandler = guildHandler;
    }
    
    public Map<User, Member> getRegistry()
    {
        return registry;
    }
    
    public Member asMember(User user)
    {
        if (user == null) return null;
        
        Member member = getRegistry().get(user);
        if (member == null)
        {
            member = new Member(guildHandler, user);
            getRegistry().put(user, member);
        }
        
        return member;
    }
}
