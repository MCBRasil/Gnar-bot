package xyz.gnarbot.gnar.utils

import org.json.JSONObject
import java.io.File
import java.util.Properties

fun File.readProperties() : Properties
{
    return Properties().apply { load(this@readProperties.inputStream()) }
}