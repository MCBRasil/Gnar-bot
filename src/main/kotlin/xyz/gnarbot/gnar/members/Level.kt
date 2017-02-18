package xyz.gnarbot.gnar.members

/**
 * Level levels for [Person] for use by the bot.
 */
enum class Level(val value: Int, val title: String, val message: String) {
    /** Highest level level. */
    BOT_CREATOR(4, "Bot Creator",
            "You need to be one of the developers for the bot to use this command."),

    /** Server owner level. */
    SERVER_OWNER(3, "Server Owner",
            "**Server Owner** commands requires you to be the server owner."),

    /** Level for members with role named "Bot Commander". */
    BOT_COMMANDER(2, "Bot Commander",
            "**Bot Commander** commands requires a role named exactly 'Bot Commander' or be the `server owner`."),

    /** Level for members with role named "DJ". */
    DJ(1, "DJ",
            "**DJ** commands requires a role named exactly 'DJ' or higher roles like 'Bot Commander' or be the `server owner`."),

    /** Default level. */
    USER(0, "User",
            "These are basic commands that all members can use, except for bots."),

    /** Level for other Discord bots. */
    BOT(-1, "Bot",
            ""),
}