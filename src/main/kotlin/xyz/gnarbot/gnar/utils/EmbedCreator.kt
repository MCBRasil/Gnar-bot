package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.requests.RestAction
import xyz.gnarbot.gnar.servers.Servlet
import java.awt.Color
import java.time.temporal.TemporalAccessor
import java.util.function.Consumer

@Suppress("NOTHING_TO_INLINE")
class EmbedCreator(private val servlet: Servlet, private val textChannel: TextChannel) {
    val delegate = EmbedBuilder()

    init {
        color(Color(0, 80, 175))
    }


    fun title(title: String?): EmbedCreator {
        return title(title, null)
    }

    fun title(title: String?, url: String?): EmbedCreator {
        delegate.setTitle(title, url)
        return this
    }



    inline fun description(block: StringBuilder.() -> Unit): EmbedCreator {
        val sb = StringBuilder()
        block(sb)
        delegate.setDescription(sb.toString())
        return this
    }

    fun description(value: String?): EmbedCreator {
        delegate.setDescription(value)
        return this
    }


    fun description(value: Any?): EmbedCreator {
        delegate.setDescription(value.toString())
        return this
    }

    // USE FOR JAVA
    fun description(block: Consumer<StringBuilder>): EmbedCreator {
        val sb = StringBuilder()
        block.accept(sb)
        delegate.setDescription(sb.toString())
        return this
    }




    inline fun field(name: String, inline: Boolean = false, block: StringBuilder.() -> Unit): EmbedCreator {
        val sb = StringBuilder()
        block(sb)
        delegate.addField(name, sb.toString(), inline)
        return this
    }

    fun field(name: String?, inline: Boolean = false, value: String?): EmbedCreator {
        delegate.addField(name, value, inline)
        return this
    }

    fun field(name: String?, inline: Boolean = false, value: Any?): EmbedCreator {
        delegate.addField(name, value.toString(), inline)
        return this
    }

    fun field(name: String?, inline: Boolean = false, block: Consumer<StringBuilder>): EmbedCreator {
        val sb = StringBuilder()
        block.accept(sb)
        delegate.addField(name, sb.toString(), inline)
        return this
    }


    fun rest(): RestAction<Note> {
        return servlet.run {
            textChannel.sendNote(build())
        }
    }


    fun timestamp(temporal: TemporalAccessor?): EmbedCreator {
        delegate.setTimestamp(temporal)
        return this
    }

    fun color(color: Color?): EmbedCreator {
        delegate.setColor(color)
        return this
    }

    fun thumbnail(url: String?): EmbedCreator {
        delegate.setThumbnail(url)
        return this
    }

    fun image(url: String?): EmbedCreator {
        delegate.setImage(url)
        return this
    }

    fun author(user: User): EmbedCreator {
        delegate.setAuthor(user.name, user.avatarUrl, user.avatarUrl)
        return this
    }

    fun author(name: String, url: String?, iconUrl: String): EmbedCreator {
        delegate.setAuthor(name, url, iconUrl)
        return this
    }

    fun footer(text: String, iconUrl: String?): EmbedCreator {
        delegate.setFooter(text, iconUrl)
        return this
    }

    fun addBlankField(inline: Boolean): EmbedCreator {
        delegate.addBlankField(inline)
        return this
    }

    fun build(): MessageEmbed {
        return delegate.build()
    }

    inline fun highlight(string: String) = b("[$string]()")
    inline fun highlight(any: Any) = highlight(any.toString())

    inline fun b(string: String) = "**$string**"
    inline fun b(any: Any?) = b(any.toString())
    inline fun i(string: String) = "*$string*"
    inline fun i(any: Any?) = i(any.toString())
    inline fun u(string: String) = "__${string}__"
    inline fun u(any: Any?) = u(any.toString())

    inline fun link(string: String, url: String? = null) = "[$string]${if (url != null) "($url)" else "()"}"
    inline fun link(any: Any, url: String? = null) = "[$any]${if (url != null) "($url)" else "()"}"
}