package xyz.gnarbot.gnar.handlers.commands;

import xyz.gnarbot.gnar.handlers.members.Clearance;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Arrays;

/**
 * Abstract class that is extended when creating a command.
 */
public abstract class CommandExecutor
{
    private String[] aliases;
    
    private String description = "No description provided.";
    
    private String usage = "No usage provided.";
    
    private Clearance clearance = Clearance.USER;
    
    private boolean shownInHelp = true;
    
    /**
     * Abstract method to be executed when the command is called.
     *
     * @param note  Message object passed into the execution.
     * @param args Arguments passed into the execution.
     */
    public abstract void execute(Note note, String label, String[] args);
    
    /**
     * Returns the aliases of the command.
     *
     * @return The aliases of the command.
     */
    public String[] getAliases()
    {
        return aliases;
    }
    
    /**
     * Set the aliases of the command.
     *
     * @param aliases Varargs list of aliases.
     */
    public void setAliases(String... aliases)
    {
        this.aliases = aliases;
    }
    
    /**
     * Returns the description of the command.
     *
     * @return The description of the command.
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Set the description of the command.
     *
     * @param desc New description.
     */
    public void setDescription(String desc)
    {
        this.description = desc;
    }
    
    /**
     * Returns the permission required to execute the command.
     *
     * @return The permission required to execute the command.
     */
    public Clearance getClearance()
    {
        return clearance;
    }
    
    /**
     * Set the permission required to execute the command.
     *
     * @param perm New permission required.
     */
    public void setClearance(Clearance perm)
    {
        this.clearance = perm;
    }
    
    /**
     * Returns the usage message of the command.
     *
     * @return The usage message of the command.
     */
    public String getUsage()
    {
        return usage;
    }
    
    /**
     * Set the usage message of the command.
     *
     * @param usage New usage message.
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }
    
    /**
     * Returns if the command is shown in _help.
     *
     * @return Flag to show this command in help.
     */
    public boolean isShownInHelp()
    {
        return shownInHelp;
    }
    
    /**
     * Set if the command is to be shown in _help.
     *
     * @param bool Is the command going to be shown in help?
     */
    public void setShownInHelp(boolean bool)
    {
        this.shownInHelp = bool;
    }
    
    /**
     * @return String representation of the command.
     */
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "(aliases=" + Arrays.toString(aliases) + ", desc=\"" + description +
                "\", usage=\"" + usage + "\", clearance=" + clearance + ", shownInHelp=" + shownInHelp + ")";
    }
}