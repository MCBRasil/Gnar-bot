package xyz.gnarbot.injection

import com.google.inject.AbstractModule
import xyz.gnarbot.Shard

class ShardInjectorModule(private val shard : Shard) : AbstractModule()
{
    public override fun configure()
    {
        bind(String::class.java).toInstance("wow")
    }
}