package xyz.gnarbot.gnar.handlers.commands;

import java.util.*;

public abstract class CommandRegistry
{
    /**
     * The mapped registry of invoking key to the classes.
     */
    private Map<String, CommandExecutor> registry = new LinkedHashMap<>();
    
    /**
     * Register and instantiate the class.
     * @param cls CommandExecutor class.
     */
    public void registerCommand(Class<? extends CommandExecutor> cls)
    {
        if (!cls.isAnnotationPresent(Command.class))
        {
            throw new IllegalStateException("@Command annotation not found for class: "+cls.getName());
        }
        
        try
        {
            CommandExecutor cmd = cls.newInstance();
            
            Command meta = cls.getAnnotation(Command.class);
            
            cmd.setAliases(meta.aliases());
            cmd.setDescription(meta.description());
            cmd.setClearance(meta.clearance());
            cmd.setShownInHelp(meta.showInHelp());
            cmd.setUsage(meta.usage());
            
            Arrays.stream(cmd.getAliases()).forEach(s -> registerCommand(s, cmd));
        }
        catch (IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Register the CommandExecutor instance into the registry.
     * @param label Invoking key.
     * @param cmd CommandExecutor instance.
     */
    public void registerCommand(String label, CommandExecutor cmd)
    {
        if (!registry.isEmpty())
        {
            for (String command : registry.keySet())
            {
                if (label.equals(command))
                {
                    // Removed until mae fucking fixes this shit
                    return;
                    //throw new IllegalStateException("Command " + label + " is already registered.");
                }
            }
        }
        
        registry.put(label, cmd);
    }
    
    /**
     * Unregisters a CommandExecutor.
     * @param label Invoking key.
     */
    public void unregisterCommand(String label) //throws IllegalStateException
    {
        if (!registry.isEmpty())
        {
            registry.keySet().stream()
                    .filter(label::equals)
                    .forEach(command -> registry.remove(label));
        }
        //throw new IllegalStateException("Command " + label + " isn't registered.");
    }
    
    /**
     * Returns the command registry.
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
        
        registry.entrySet().stream()
                .filter(entry -> !blacklist.contains(entry.getValue()))
                .forEach(entry ->
                {
                    uniqueMap.put(entry.getKey(), entry.getValue());
                    blacklist.add(entry.getValue());
                });
        
        return uniqueMap;
    }
    
    /**
     * Get the command based on the key.
     * @param key Invoking key.
     * @return CommandExecutor instance.
     */
    public CommandExecutor getCommand(String key)
    {
        return getRegistry().get(key);
    }
    
    /**
     * Returns the CommandExecutor based on the class.
     * @param cls CommandExecutor class.
     * @return CommandExecutor instance.
     */
    public CommandExecutor getCommand(Class<? extends CommandExecutor> cls)
    {
        Optional<CommandExecutor> cmd = registry.values().stream()
                .filter(commandExecutor -> commandExecutor.getClass() == cls)
                .findFirst();
        
        return cmd.isPresent() ? cmd.get() : null;
    }
}
