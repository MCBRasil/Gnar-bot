package xyz.gnarbot.gnar.commands.handlers;

import java.util.*;

public abstract class CommandRegistry
{
    /**
     * The mapped registry of invoking key to the classes.
     */
    private Map<String, CommandExecutor> registry = new LinkedHashMap<>();
    
    /**
     * Register the CommandExecutor instance into the registry.
     *
     * @param label Invoking key.
     * @param cmd   CommandExecutor instance.
     */
    public CommandExecutor registerCommand(String label, CommandExecutor cmd)
    {
        if (registry.containsKey(label))
        {
            throw new IllegalStateException("Command " + label + " is already registered.");
        }
        
        if (cmd.isSeparate())
        {
            try
            {
                CommandExecutor copy = cmd.copy();
                
                for (String s : copy.getAliases())
                {
                    registry.put(s, copy);
                }
                
                return copy;
            }
            catch (IllegalAccessException | InstantiationException e)
            {
                e.printStackTrace();
            }
        }
        
        return registry.put(label, cmd);
    }
    
    /**
     * Unregisters a CommandExecutor.
     *
     * @param label Invoking key.
     */
    public void unregisterCommand(String label) //throws IllegalStateException
    {
        registry.remove(label);
    }
    
    /**
     * Returns the command registry.
     *
     * @return The command registry.
     */
    public Map<String, CommandExecutor> getRegistry()
    {
        return registry;
    }
    
    public Map<String, CommandExecutor> getUniqueRegistry()
    {
        Set<CommandExecutor> blacklist = new HashSet<>();
        Map<String, CommandExecutor> uniqueMap = new LinkedHashMap<>();
        
        registry.entrySet().stream().filter(entry -> !blacklist.contains(entry.getValue())).forEach(entry ->
        {
            uniqueMap.put(entry.getKey(), entry.getValue());
            blacklist.add(entry.getValue());
        });
        
        return uniqueMap;
    }
    
    /**
     * Get the command based on the key.
     *
     * @param key Invoking key.
     *
     * @return CommandExecutor instance.
     */
    public CommandExecutor getCommand(String key)
    {
        return getRegistry().get(key);
    }
    
    /**
     * Returns the CommandExecutor based on the class.
     *
     * @param cls CommandExecutor class.
     *
     * @return CommandExecutor instance.
     */
    public CommandExecutor getCommand(Class<? extends CommandExecutor> cls)
    {
        Optional<CommandExecutor> cmd = registry.values()
                .stream()
                .filter(commandExecutor -> commandExecutor.getClass() == cls)
                .findFirst();
        
        return cmd.isPresent() ? cmd.get() : null;
    }
}
