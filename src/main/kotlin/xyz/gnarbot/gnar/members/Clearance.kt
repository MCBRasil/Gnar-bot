package xyz.gnarbot.gnar.members

/**
 * Clearance levels for [Person] for use by the bot.
 */
enum class Clearance(val value: Int) {
    /** Highest level clearance. */
    BOT_MASTER(4),

    /** Server owner clearance. */
    SERVER_OWNER(3),

    /** Clearance for members with role named "Bot Commander". */
    BOT_COMMANDER(2),

    /** Clearance for members with role named "DJ". */
    DJ(1),

    /** Default clearance. */
    USER(0),

    /** Clearance for other Discord bots. */
    BOT(-1),

}