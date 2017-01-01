@file:JvmName("KUtils")

package xyz.gnarbot.gnar.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed
import xyz.gnarbot.gnar.Bot
import java.awt.Color
import java.io.File
import java.util.Properties

fun File.readProperties() : Properties
{
    return Properties().apply { load(this@readProperties.inputStream()) }
}

fun File.child(path : String) = File(this, path)

@JvmOverloads
fun makeEmbed(title : String, msg : String, color : Color = Bot.color, thumb : String? = null, img : String? = null) : MessageEmbed
{
    return EmbedBuilder().run {
        setDescription(msg)
        setTitle(title)
        setColor(color)
        
        if (!thumb.isNullOrBlank())
            setThumbnail(thumb)
        
        if (!img.isNullOrBlank())
            setImage(img)
        
        build()
    }
}

fun makeEmbed(title : String, msg : String, color : Color, img : String, imageNotThumb : Boolean) : MessageEmbed
{
    return EmbedBuilder().run {
        setDescription(msg)
        setTitle(title)
        setColor(color)
    
        if (!img.isNullOrBlank())
        {
            if (imageNotThumb)
            {
                setImage(img)
            }
            else
            {
                setThumbnail(img)
            }
        }
        
        build()
    }
}