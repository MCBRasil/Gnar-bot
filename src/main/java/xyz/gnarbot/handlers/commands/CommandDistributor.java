package xyz.gnarbot.handlers.commands;

import com.google.inject.Inject;
import xyz.gnarbot.Shard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDistributor
{
    private final Shard shard;
    
    enum CommandType
    {
        INDEPENDENT,
        MANAGED,
        GUILD
    }
    
    private final Map<String, CommandExecutor> globalCMDRegistry = new HashMap<>();
    private final List<Class<? extends CommandExecutor>> managerCMDRegistry = new ArrayList<>();
    private final List<Class<? extends CommandExecutor>> guildCMDRegistry = new ArrayList<>();
    
    public CommandDistributor(Shard shard)
    {
        this.shard = shard;
    }
    
    public void register(Class<? extends CommandExecutor> cls)
    {
        
    }
    
    public CommandType classify(Class<? extends CommandExecutor> cls)
    {
        return CommandType.INDEPENDENT;
    }
    
    /**
     * Find all field within a class that is annotated with @Inject.
     *
     * @see Inject
     *
     * @param cls Target class.
     * @return Fields in target class annotated with {@link Inject @Inject}.
     */
    public static Field[] findInjectableFields(Class<?> cls)
    {
        List<Field> fields = new ArrayList<>();
        
        for (Field field : cls.getDeclaredFields())
        {
            if (field.isAnnotationPresent(Inject.class))
            {
                fields.add(field);
            }
        }
        
        return fields.toArray(new Field[fields.size()]);
    }
}
