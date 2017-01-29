package xyz.gnarbot.gnar.commands.handlers;

import java.util.*;

public abstract class CommandRegistry {
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
    public CommandExecutor registerCommand(String label, CommandExecutor cmd) {
        label = label.toLowerCase();
        if (registry.containsKey(label)) {
            throw new IllegalStateException("Command " + label + " is already registered.");
        }

        if (cmd.isCopy()) {
            try {
                CommandExecutor copy = cmd.copy();

                for (String s : copy.getAliases()) {
                    registry.put(s.toLowerCase(), copy);
                }

                return copy;
            } catch (IllegalAccessException | InstantiationException e) {
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
    public Map<String, CommandExecutor> getRegistry() {
        return registry;
    }

    @Deprecated
    public Map<String, CommandExecutor> getUniqueRegistry() {
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
     * @return Unique command executors.
     */
    public Set<CommandExecutor> getUniqueExecutors() {
        Set<CommandExecutor> set = new LinkedHashSet<>();
        for (CommandExecutor cmd : registry.values()) {
            set.add(cmd);
        }
        return set;
    }

    /**
     * Get the command based on the key.
     *
     * @param key Invoking key.
     * @return CommandExecutor instance.
     */
    public CommandExecutor getCommand(String key) {
        return getRegistry().get(key);
    }

    /**
     * Returns the CommandExecutor based on the class.
     *
     * @param cls CommandExecutor class.
     * @return CommandExecutor instance.
     */
    public CommandExecutor getCommand(Class<? extends CommandExecutor> cls) {
        Optional<CommandExecutor> cmd = registry.values()
                .stream()
                .filter(commandExecutor -> commandExecutor.getClass() == cls)
                .findFirst();

        return cmd.isPresent() ? cmd.get() : null;
    }
}
