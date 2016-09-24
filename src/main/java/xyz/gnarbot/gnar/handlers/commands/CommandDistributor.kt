package xyz.gnarbot.gnar.handlers.commands

import com.google.inject.Inject
import net.dv8tion.jda.entities.Guild
import xyz.gnarbot.gnar.Host
import xyz.gnarbot.gnar.handlers.MemberHandler
import java.lang.reflect.Field
import java.util.ArrayList
import java.util.HashMap

class CommandDistributor
{
    /**
     * Command type classifications.
     */
    enum class CommandType
    {
        SINGLETON,
        MANAGED
    }
    
    val singletonCommands = HashMap<String, CommandExecutor>()
    val managedCommands = mutableListOf<Class<out CommandExecutor>>()
    
    /**
     * Register the command class and automatically
     * classify the command.
     *
     * @param cls Class to register.
     */
    fun register(cls : Class<out CommandExecutor>)
    {
        if (!cls.isAnnotationPresent(Command::class.java))
        {
            throw IllegalStateException("@Command annotation not found for class: " + cls.name)
        }
        
        when (classify(cls))
        {
            CommandType.SINGLETON ->    registerAsSingleton(cls)
            CommandType.MANAGED ->      registerAsManaged(cls)
        }
    }
    
    /**
     * Register command as a singleton command.
     *
     * @param cls Class to register.
     */
    fun registerAsSingleton(cls : Class<out CommandExecutor>)
    {
        val cmd = cls.newInstance()
    
        val meta = cls.getAnnotation(Command::class.java)
        
        cmd.setAliases(*meta.aliases)
        cmd.description = meta.description
        cmd.clearance = meta.clearance
        cmd.isShownInHelp = meta.showInHelp
        cmd.usage = meta.usage
    
        for (alias in cmd.aliases)
        {
            singletonCommands.put(alias, cmd)
        }
    }
    
    /**
     * Register command as a managed command.
     *
     * @param cls Class to register.
     */
    fun registerAsManaged(cls : Class<out CommandExecutor>) = managedCommands.add(cls)
    
    private companion object
    {
        /**
         * Find all field within a class that is annotated with @Inject.
         *
         * @see Inject
         * @param cls Target class.
         * @return Fields in target class annotated with [@Inject][Inject].
         */
        @JvmStatic fun findInjectableFields(cls : Class<*>) : Array<Field>
        {
            val fields = ArrayList<Field>()
            
            for (field in cls.declaredFields)
            {
                if (field.isAnnotationPresent(Inject::class.java))
                {
                    fields.add(field)
                }
            }
            
            return fields.toTypedArray()
        }
        
        /**
         * Classify the command based on it's injectable fields.
         *
         * @param cls Target class.
         * @return Command type.
         */
        @JvmStatic fun classify(cls : Class<out CommandExecutor>) : CommandType
        {
            val types = findInjectableFields(cls).map { it.type }
        
            return when
            {
                types.contains(Guild::class.java) ||
                        types.contains(Host::class.java) ||
                        types.contains(CommandHandler::class.java) ||
                        types.contains(MemberHandler::class.java) -> CommandType.MANAGED
                else -> CommandType.SINGLETON
            }
        }
    }
}
