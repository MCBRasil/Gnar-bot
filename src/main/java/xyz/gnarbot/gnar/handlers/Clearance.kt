package xyz.gnarbot.gnar.handlers

/**
 * Clearance levels for [Member] for use by the bot.
 */
enum class Clearance(val value : Int)
{
    /** Highest level clearance. */
    BOT_MASTER(3),
    
    /** Server owner clearance. */
    SERVER_OWNER(2),
    
    /** Clearance for members with role named "Bot Commander". */
    BOT_COMMANDER(1),
    
    /** Default clearance. */
    USER(0),
    
    /** Clearance for other Discord bots. */
    BOT(-1)
}