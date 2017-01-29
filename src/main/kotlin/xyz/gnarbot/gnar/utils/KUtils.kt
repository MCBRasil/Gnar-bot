@file:JvmName("KUtils")

package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.Person
import java.awt.Color
import java.io.File
import java.util.*

fun File.readProperties(): Properties {
    return Properties().apply { load(this@readProperties.inputStream()) }
}

fun File.child(path: String) = File(this, path)

@JvmOverloads
fun makeEmbed(title: String?, msg: String?, color: Color? = Bot.color, thumb: String? = null, img: String? = null, author: Person? = null): MessageEmbed {
    return EmbedBuilder().run {
        setDescription(msg)
        setTitle(title)
        setColor(color)

        if (author != null) {
            setAuthor(author.name, null, author.avatarUrl)
        }

        setThumbnail(thumb)

        setImage(img)

        build()
    }
}