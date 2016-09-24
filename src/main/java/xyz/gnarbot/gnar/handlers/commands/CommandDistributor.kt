package xyz.gnarbot.gnar.handlers.commands

import com.google.inject.Inject
import net.dv8tion.jda.entities.Guild
import xyz.gnarbot.gnar.GuildHandler
import xyz.gnarbot.gnar.handlers.MemberHandler
import java.lang.reflect.Field
import java.util.ArrayList
import java.util.HashMap

class CommandDistributor
{
    enum class CommandType
    {
        SINGLETON,
        MANAGED
    }
    
    val singletonCommands = HashMap<String, CommandExecutor>()
    val managedCommands = mutableListOf<Class<out CommandExecutor>>()
    
    fun register(cls : Class<out CommandExecutor>)
    {
        when (classify(cls))
        {
            CommandType.SINGLETON ->    registerCommand(cls)
            CommandType.MANAGED ->      managedCommands.add(cls)
        }
    }
    
    fun classify(cls : Class<out CommandExecutor>) : CommandType
    {
        val types = findInjectableFields(cls).map { it.type }
        
        return when
        {
            types.contains(Guild::class.java) ||
            types.contains(GuildHandler::class.java) ||
            types.contains(CommandHandler::class.java) ||
            types.contains(MemberHandler::class.java) -> CommandType.MANAGED
            else -> CommandType.SINGLETON
        }
    }
    
    fun registerCommand(cls : Class<out CommandExecutor>)
    {
        val cmd = cls.newInstance()
    
        val meta = cls.getAnnotation(Command::class.java)
    
        cmd.description = meta.description
        cmd.clearance = meta.clearance
        cmd.isShownInHelp = meta.showInHelp
        cmd.usage = meta.usage
    
        for (alias in meta.aliases)
        {
            singletonCommands.put(alias, cmd)
        }
    }
    
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
    }
}
