package xyz.gnarbot.gnar.members

/**
 * Clearance levels for [Person] for use by the bot.
 */
enum class Level(val value: Int, val title: String) {
    /** Highest level clearance. */
    BOT_MASTER(4, "Bot Master"),

    /** Server owner clearance. */
    SERVER_OWNER(3, "Server Owner"),

    /** Clearance for members with role named "Bot Commander". */
    BOT_COMMANDER(2, "Bot Commander"),

    /** Clearance for members with role named "DJ". */
    DJ(1, "DJ"),

    /** Default clearance. */
    USER(0, "User"),

    /** Clearance for other Discord bots. */
    BOT(-1, "Bot"),
}