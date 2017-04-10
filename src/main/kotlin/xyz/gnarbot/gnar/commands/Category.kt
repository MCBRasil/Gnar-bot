package xyz.gnarbot.gnar.commands

enum class Category(val title: String, val description: String = "", val show: Boolean = true) {
    BETA("Beta", "Beta commands. Not complete. Don't complain.", show = false),
    MODERATION("Moderation", "Commands that help with the regulating normal server tasks"),
    MUSIC("Music", "Listen to the bot's fabulous renditions of your favorite songs."),
    FUN("Fun", "Everything here is memes. Dank unfiltered memes. Batteries not included."),
    GENERAL("General", "General bot commands that every lame bot has. Have fun!"),
    NONE("", show = false),
}