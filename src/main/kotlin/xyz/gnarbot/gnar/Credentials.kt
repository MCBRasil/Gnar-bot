package xyz.gnarbot.gnar

import org.json.JSONObject
import java.io.File
import kotlin.jvm.JvmStatic as static

object Credentials {
    private val file = File(Bot.files.data, "credentials.json")
    private val jso = JSONObject(file.readText())

    private val token = jso.getJSONObject("token")
    @JvmField val PRODUCTION = token.getString("production")
    @JvmField val BETA = token.optString("beta")

    private val marvel = jso.optJSONObject("marvel")
    @JvmField val MARVEL_PU: String? = marvel.optString("pu")
    @JvmField val MARVEL_PR: String? = marvel.optString("pr")

    private val abal = jso.optJSONObject("abal")
    @JvmField val ABAL_URL: String? = marvel.optString("url")
    @JvmField val ABAL_TOKEN: String? = marvel.optString("token")

    @JvmField val LEAUGE: String? = jso.optString("leauge")
    @JvmField val IMGFLIP: String? = jso.optString("imgflip")

    @JvmField val CARBONITEX: String? = jso.optString("carbonitex")

    @JvmField val MASHAPE: String? = jso.optString("mashape")
}