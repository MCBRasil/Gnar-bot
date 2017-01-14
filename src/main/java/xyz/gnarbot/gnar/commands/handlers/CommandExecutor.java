package xyz.gnarbot.gnar.commands.handlers;

import xyz.gnarbot.gnar.members.Clearance;
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
    
    private boolean separate = false;
    
    private boolean injected = false;
    
    /**
     * Abstract method to be executed when the command is called.
     *
     * @param note Message object passed into the execution.
     * @param args Arguments passed into the execution.
     */
    public abstract void execute(Note note, String label, String[] args);
    
    /**
     * @return The aliases of the command.
     */
    public String[] getAliases()
    {
        return aliases;
    }
    
    /**
     * @param aliases Varargs list of aliases.
     */
    public void setAliases(String... aliases)
    {
        this.aliases = aliases;
    }
    
    /**
     * @return The description of the command.
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * @param desc New description.
     */
    public void setDescription(String desc)
    {
        this.description = desc;
    }
    
    /**
     * @return The permission required to execute the command.
     */
    public Clearance getClearance()
    {
        return clearance;
    }
    
    /**
     * @param perm New permission required.
     */
    public void setClearance(Clearance perm)
    {
        this.clearance = perm;
    }
    
    /**
     * @return The usage message of the command.
     */
    public String getUsage()
    {
        return usage;
    }
    
    /**
     * @param usage New usage message.
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }
    
    /**
     * @return Flag to show this command in help.
     */
    public boolean isShownInHelp()
    {
        return shownInHelp;
    }
    
    /**
     * @param shown Is the command going to be shown in help?
     */
    public void setShownInHelp(boolean shown)
    {
        this.shownInHelp = shown;
    }
    
    /**
     * @return If the command require a separate instance.
     */
    public boolean isSeparate()
    {
        return this.separate;
    }
    
    /**
     * Set if the command require a separate instance.
     */
    public void setSeparate(boolean separate)
    {
        this.separate = separate;
    }
    
    /**
     * @return if the command have fields that needs
     * to be injected.
     */
    public boolean isInjected()
    {
        return this.injected;
    }
    
    /**
     * Set if the command have fields that needs
     * to be injected.
     */
    public void setInjected(boolean injected)
    {
        this.injected = injected;
    }
    
    /**
     * @return String representation of the command.
     */
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "(aliases=" + Arrays.toString(aliases) + ", desc=\"" + description + "\", " +
                "usage=\"" + usage + "\", clearance=" + clearance + ", shownInHelp=" + shownInHelp + ")";
    }
    
    public CommandExecutor copy() throws IllegalAccessException, InstantiationException
    {
        CommandExecutor copy = this.getClass().newInstance();
        copy.aliases = aliases;
        copy.usage = usage;
        copy.description = description;
        copy.clearance = clearance;
        copy.shownInHelp = shownInHelp;
        copy.separate = separate;
        copy.injected = injected;
        return copy;
    }
}