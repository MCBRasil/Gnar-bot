@file:JvmName("Extensions")

package xyz.gnarbot.gnar.utils

import java.io.File
import java.util.*

fun File.readProperties() : Properties
{
    return Properties().apply { load(this@readProperties.inputStream()) }
}

fun File.child(path : String) = File(this, path)