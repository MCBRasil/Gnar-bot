package xyz.gnarbot;

import com.google.inject.Guice;
import com.google.inject.Injector;
import xyz.gnarbot.injection.ShardInjectorModule;

/**
 * Individual sharded instances of the bot.
 */
public class Shard
{
    private final Injector injector;
    
    public Shard()
    {
        injector = Guice.createInjector(new ShardInjectorModule(this));
    }
}
