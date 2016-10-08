package xyz.gnarbot.gnar.handlers.members;

import net.dv8tion.jda.entities.User;
import xyz.gnarbot.gnar.handlers.servers.Host;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle {@link Member Member} instances.
 */
public class MemberHandler
{
    private final Host host;
    private final Map<User, Member> registry = new HashMap<>();
    
    public MemberHandler(Host host)
    {
        this.host = host;
    }
    
    /**
     * Returns the wrapper mapping registry.
     * @return The wrapper mapping registry.
     */
    public Map<User, Member> getRegistry()
    {
        return registry;
    }
    
    /**
     * Lazily wrap users in a Member instance.
     *
     * @param user JDA user.
     * @return Member instance.
     */
    public Member asMember(User user)
    {
        if (user == null) return null;
        
        Member member = getRegistry().get(user);
        if (member == null)
        {
            member = new Member(host, user);
            getRegistry().put(user, member);
        }
        
        return member;
    }
}
