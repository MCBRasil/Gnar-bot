package xyz.gnarbot.injection;

public class Injector
{
    private final Object[] objects;
    
    public Injector(Object... objects)
    {
        this.objects = objects;
    }
    
    public <T> T inject(T target)
    {
        InjectHandler.injectFields(target, objects);
        return target;
    }
}
