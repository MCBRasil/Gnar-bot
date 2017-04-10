package xyz.gnarbot.gnar.api.data

import net.dv8tion.jda.core.JDA
import xyz.gnarbot.gnar.guilds.Shard

class ShardInfo(shard: Shard) {
    val requests: Int = shard.guildData.values.map { it.commandHandler.requests }.sum()
    val id: Int = shard.id
    val status: JDA.Status = shard.status
    val guilds: Int = shard.guilds.size
    val users: Int = shard.users.size
    val textChannels: Int = shard.textChannels.size
    val voiceChannels: Int = shard.voiceChannels.size
}