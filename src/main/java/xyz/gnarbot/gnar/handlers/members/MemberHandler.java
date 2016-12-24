package xyz.gnarbot.gnar.handlers.members;

import net.dv8tion.jda.core.entities.Member;
import xyz.gnarbot.gnar.handlers.servers.Host;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle {@link User Member} instances.
 */
public class MemberHandler
{
    private final Host host;
    
    private final Map<Member, User> registry = new HashMap<>();
    
    public MemberHandler(Host host)
    {
        this.host = host;
    }
    
    /**
     * Returns the wrapper mapping registry.
     *
     * @return The wrapper mapping registry.
     */
    public Map<Member, User> getRegistry()
    {
        return registry;
    }
    
    /**
     * Lazily wrap users in a Member instance.
     *
     * @param member JDA member.
     *
     * @return User instance.
     */
    public User asUser(Member member)
    {
        if (member == null) return null;
    
        return getRegistry().computeIfAbsent(member, k -> new User(host, member));
    }
    
    /**
     * Lazily wrap users in a Member instance.
     *
     * @param user0 JDA user.
     *
     * @return User instance.
     */
    public User asUser(net.dv8tion.jda.core.entities.User user0)
    {
        if (user0 == null) return null;
        
        Member member = host.getMember(user0);
        
        return asUser(member);
    }
}

