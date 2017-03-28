package xyz.gnarbot.gnar.commands.executors.music.parent

import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import java.awt.Color

abstract class MusicExecutor : CommandExecutor() {
    val musicColor = Color(0, 221, 88)

    protected fun getTimestamp(milliseconds: Long): String {
        val seconds = milliseconds / 1000 % 60
        val minutes = milliseconds / (1000 * 60) % 60
        val hours = milliseconds / (1000 * 60 * 60) % 24

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            return String.format("%02d:%02d", minutes, seconds)
        }
    }
}
