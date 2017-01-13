package xyz.gnarbot.gnar.commands.executors.polls;

public class Poll
{
    private int pollid;
    
    public Poll()
    {
        // Poll identifier.
    }
    
    public int getPollid()
    {
        return pollid;
    }
    
    public void setPollid(int pollid)
    {
        this.pollid = pollid;
    }
    
    public void startPoll() {}
}
