package xyz.gnarbot.gnar.commands.handlers

enum class Category(val title: String, val description: String = "", val show: Boolean = true) {
    BETA("Beta commands. Not complete. Don't complain."),
    MODERATION("Moderation", description = "Commands that help with the regulating normal server tasks"),
    MUSIC("Music", description = "Listen to the bot's fabulous renditions of your favorite songs."),
    FUN("Fun", description = "Everything here is memes. Dank unfiltered memes. Batteries not included."),
    GENERAL("General", description = "General bot commands that every lame bot has. Have fun!"),
    NONE("", show = false),
}