package xyz.gnarbot.gnar.handlers.commands;

import xyz.gnarbot.gnar.handlers.Clearance;
import xyz.gnarbot.gnar.utils.Note;

/**
 * Abstract class that is extended when creating a command.
 */
public abstract class CommandExecutor
{
    private String description = "No descriptions provided.";
    private String usage = null;
    private Clearance clearance = Clearance.USER;
    private boolean showInHelp = true;
    
    /**
     * Abstract method to be executed when the command is called.
     *
     * @param msg Message object passed into the execution.
     * @param args Arguments passed into the execution.
     */
    public abstract void execute(Note msg, String label, String[] args);
    
    /**
     * Returns the description of the command.
     * @return The description of the command.
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Set the description of the command.
     * @param desc New description.
     */
    public void setDescription(String desc)
    {
        this.description = desc;
    }
    
    /**
     * Returns the permission required to execute the command.
     * @return The permission required to execute the command.
     */
    public Clearance getClearance()
    {
        return clearance;
    }
    
    /**
     * Set the permission required to execute the command.
     * @param perm New permission required.
     */
    public void setClearance(Clearance perm)
    {
        this.clearance = perm;
    }
    
    /**
     * Returns the usage message of the command.
     * @return The usage message of the command.
     */
    public String getUsage()
    {
        return usage;
    }
    
    /**
     * Set the usage message of the command.
     * @param usage New usage message.
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }
    
    /**
     * Returns if the command is shown in _help.
     * @return Flag to show this command in help.
     */
    public boolean isShownInHelp()
    {
        return showInHelp;
    }
    
    /**
     * Set if the command is to be shown in _help.
     * @param bool Is the command going to be shown in help?
     */
    public void setShownInHelp(boolean bool)
    {
        this.showInHelp = bool;
    }
}