package xyz.gnarbot.gnar

import xyz.gnarbot.gnar.utils.child
import java.io.File

class BotFiles {
    /** _data folder. */
    val data = File("_DATA")
            .apply { if (!exists()) mkdir() }

    /** _data/hosts folder. */
    //    val hosts = data
//            .child("host")
//            .apply { if (!exists()) mkdir() }

    /** tokens.properties file. */
    val tokens = data
            .child("tokens.properties")
            .apply { if (!exists()) throw IllegalStateException("`$path` do not exist.") }

    val admins = data
            .child("administrators")
            .apply { if (!exists()) throw IllegalStateException("`$path` do not exist.") }
}