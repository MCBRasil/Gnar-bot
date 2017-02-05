package xyz.gnarbot.gnar.members

/**
 * Level levels for [Person] for use by the bot.
 */
enum class Level(val value: Int, val title: String) {
    /** Highest level level. */
    BOT_MASTER(4, "Bot Master"),

    /** Server owner level. */
    SERVER_OWNER(3, "Server Owner"),

    /** Level for members with role named "Bot Commander". */
    BOT_COMMANDER(2, "Bot Commander"),

    /** Level for members with role named "DJ". */
    DJ(1, "DJ"),

    /** Default level. */
    USER(0, "User"),

    /** Level for other Discord bots. */
    BOT(-1, "Bot"),
}