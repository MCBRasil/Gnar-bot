package xyz.gnarbot.gnar.tests;

import junit.framework.TestCase;
import xyz.gnarbot.gnar.Host;
import xyz.gnarbot.gnar.Shard;
import xyz.gnarbot.gnar.fakes.FakeGuild;
import xyz.gnarbot.gnar.fakes.FakeJDA;
import xyz.gnarbot.gnar.fakes.FakeMessage;

public class JavaTests extends TestCase
{
    private Shard shard = new Shard(0, FakeJDA.INSTANCE);
    private Host host = shard.getHost(FakeGuild.INSTANCE);
    
    public void testTestCommand()
    {
        sendToHost("_wow");
    }
    
    public void sendToHost(String string)
    {
        host.handleMessageEvent(FakeMessage.createEvent(string));
    }
}
