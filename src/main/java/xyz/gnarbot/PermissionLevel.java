package xyz.gnarbot;

public enum PermissionLevel
{
    BOT_MASTER(3),
    SERVER_OWNER(2),
    BOT_COMMANDER(1),
    USER(0),
    BOT(-1);
    
    public final int value;
    
    PermissionLevel(int level)
    {
        this.value = level;
    }
}
