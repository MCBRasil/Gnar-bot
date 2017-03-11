@file:JvmName("KUtils")
@file:Suppress("NOTHING_TO_INLINE")

package xyz.gnarbot.gnar.utils

import java.io.File
import java.util.*

inline fun File.child(path: String) = File(this, path)

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