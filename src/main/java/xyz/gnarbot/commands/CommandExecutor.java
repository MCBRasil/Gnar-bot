package xyz.gnarbot.commands;

import net.dv8tion.jda.entities.Message;
import xyz.gnarbot.PermissionLevel;

public abstract class CommandExecutor
{
    private CommandManager manager;
    
    private String description = "No descriptions provided.";
    private String usage = null;
    private PermissionLevel permission = PermissionLevel.USER;
    private boolean showInHelp = true;
    
    public abstract void execute(Message msg, String[] args);
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String desc)
    {
        this.description = desc;
    }
    
    public PermissionLevel getPermission()
    {
        return permission;
    }
    
    public void setPermission(PermissionLevel perm)
    {
        this.permission = perm;
    }
    
    public String getUsage()
    {
        return usage;
    }
    
    public void setUsage(String usage)
    {
        this.usage = usage;
    }
    
    public boolean isShownInHelp()
    {
        return showInHelp;
    }
    
    public void showInHelp(boolean bool)
    {
        this.showInHelp = bool;
    }
}