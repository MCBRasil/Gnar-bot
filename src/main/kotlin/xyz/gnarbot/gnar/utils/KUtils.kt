@file:JvmName("KUtils")

package xyz.gnarbot.gnar.utils

import java.io.File
import java.util.*
import java.util.function.Consumer

@Suppress("NOTHING_TO_INLINE")
inline fun File.child(path: String) = File(this, path)

//inline fun TextChannel.embed(title: String? = null, value: EmbedCreator.() -> Unit): EmbedCreator {
//    return EmbedCreator(servlet, this).title(title).apply { value(this) }
//}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> Consumer<T>.invoke(value: T) {
    this.accept(value)
}

fun String.fastSplit(delimiter: Char): List<String> {
    val res = ArrayList<String>(count { it == delimiter } + 1)
    var i = 0
    var p = 0

    while (i < this.length) {
        if (this[i] == delimiter) {
            res += this.substring(p, i)
            p = i + 1
        }

        while (i < this.length - 1 && this[i + 1] == delimiter) {
            i++
            p++
        }

        i++
    }
    res += this.substring(p)

    return res
}