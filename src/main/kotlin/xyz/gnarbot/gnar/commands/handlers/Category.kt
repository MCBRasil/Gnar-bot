package xyz.gnarbot.gnar.commands.handlers

enum class Category(val title: String, val show: Boolean = true) {
    MODERATION("Moderation"),
    MUSIC("Music"),
    FUN("Fun"),
    GENERAL("General"),
    NONE("", false),
}