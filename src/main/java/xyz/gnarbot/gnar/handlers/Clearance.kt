package xyz.gnarbot.gnar.handlers

/**
 * Permission levels for [Member] handled by the bot.
 */
enum class Clearance(val value : Int)
{
    BOT_MASTER(3),
    SERVER_OWNER(2),
    BOT_COMMANDER(1),
    USER(0),
    BOT(-1)
}