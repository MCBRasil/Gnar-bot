package xyz.gnarbot.gnar.tests;

import junit.framework.TestCase;
import xyz.gnarbot.gnar.fakes.FakeBot;

public class JavaTests extends TestCase
{
    public void testTestCommand()
    {
        FakeBot.INSTANCE.send("_wow");
    }
}
