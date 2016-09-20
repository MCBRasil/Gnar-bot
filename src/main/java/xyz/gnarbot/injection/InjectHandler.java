package xyz.gnarbot.injection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class InjectHandler
{
    /**
     * Find all field within a class that is annotated with @Inject.
     *
     * @see Inject
     *
     * @param cls Target class.
     * @return Fields in target class annotated with {@link Inject @Inject}.
     */
    public static Field[] findFields(Class<?> cls)
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
    
    /**
     * Inject specified objects into all possible {@link Inject injectable} fields.
     *
     * @see Inject
     *
     * @param target Target object.
     * @param objects Objects to inject into target.
     */
    public static void injectFields(Object target, Object... objects)
    {
        try
        {
            for (Field field : findFields(target.getClass()))
            {
                for (Object obj : objects)
                {
                    if (field.getType() == obj.getClass())
                    {
                        field.setAccessible(true);
                        field.set(target, obj);
                    }
                }
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}
