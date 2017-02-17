@file:JvmName("KUtils")

package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.HostUser
import java.awt.Color
import java.io.File
import java.util.*

fun File.readProperties(): Properties {
    return Properties().apply { load(this@readProperties.inputStream()) }
}

fun File.child(path: String) = File(this, path)

fun String.fastSplit(char: Char): List<String> {
    val res = ArrayList<String>(count { it == char } + 1)
    var i = 0
    var p = 0

    while (i < this.length) {
        if (this[i] == char) {
            res += this.substring(p, i)
            p = i + 1
        }
        i++
    }
    res += this.substring(p)

    return res
}

@JvmOverloads
fun makeEmbed(title: String?, msg: String?, color: Color? = Bot.color, thumb: String? = null, img: String? = null, author: HostUser? = null): MessageEmbed {
    return EmbedBuilder().run {
        setDescription(msg)
        setTitle(title, null)
        setColor(color)

        if (author != null) {
            setAuthor(author.name, null, author.avatarUrl)
        }

        setThumbnail(thumb)

        setImage(img)

        build()
    }
}