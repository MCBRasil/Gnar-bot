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
            "**Server Owner** commands requires you to be the __[server owner]()__."),

    /** Level for members with role named "Bot Commander". */
    BOT_COMMANDER(2, "Bot Commander",
            "**Bot Commander** commands requires a role named exactly __['Bot Commander']()__ or be the __[server owner]()__."),

    /** Level for members with role named "DJ". */
    DJ(1, "DJ",
            "**DJ** commands requires a role named __['DJ']()__ or higher roles like __['Bot Commander']()__ or be the __[server owner]()__."),

    /** Default level. */
    USER(0, "User",
            "These are basic commands that all members, except bots, can use."),

    /** Level for other Discord bots. */
    BOT(-1, "Bot",
            ""),
}