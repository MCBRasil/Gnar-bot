@file:JvmName("KUtils")

package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.EntityBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.entities.impl.MessageImpl
import net.dv8tion.jda.core.requests.Request
import net.dv8tion.jda.core.requests.Response
import net.dv8tion.jda.core.requests.RestAction
import net.dv8tion.jda.core.requests.Route
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.members.Client
import xyz.gnarbot.gnar.servers.Servlet
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
fun makeEmbed(title: String?, msg: String?, color: Color? = Bot.color, thumb: String? = null, img: String? = null, author: Client? = null): MessageEmbed {
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

fun MessageChannel.sendNote(servlet: Servlet, embed: MessageEmbed) : RestAction<Note> {
    val message = MessageBuilder().setEmbed(embed).build()
    return sendNote(servlet, message)
}

fun MessageChannel.sendNote(servlet: Servlet, message: Message) : RestAction<Note> {
    val route = Route.Messages.SEND_MESSAGE.compile(id)
    val json = (message as MessageImpl).toJSONObject()
    return object : RestAction<Note>(jda, route, json) {
        override fun handleResponse(response: Response, request: Request<Any?>?) {
            if (response.isOk) {
                val msg = EntityBuilder.get(jda).createMessage(response.`object`, this@sendNote, false)
                request?.onSuccess(Note(servlet, msg))
            } else {
                request?.onFailure(response)
            }
        }
    }
}