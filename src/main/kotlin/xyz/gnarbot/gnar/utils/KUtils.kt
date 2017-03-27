@file:JvmName("KUtils")
@file:Suppress("NOTHING_TO_INLINE")

package xyz.gnarbot.gnar.utils

import java.io.File
import java.util.*
import java.util.function.Consumer

inline fun File.child(path: String) = File(this, path)