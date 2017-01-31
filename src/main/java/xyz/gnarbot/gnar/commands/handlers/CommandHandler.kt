package xyz.gnarbot.gnar.commands.handlers

import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.servers.Host
import xyz.gnarbot.gnar.utils.fastSplit

class CommandHandler(private val host: Host) : CommandRegistry() {

    /**
     * Return the amount of successful requests on this command handler.

     * @return the amount of successful requests on this command handler.
     */
    var requests = 0
        private set

    /**
     * Extract command classes/instances from CommandDistributor
     * and register it in this handler.

     * @param distributor CommandDistributor instance.
     */
    fun receiveFrom(distributor: CommandTable) {
        distributor.commands.forEach {
            this.registerCommand(it.key, it.value)
        }
    }

    /**
     * Call the command based on the message content.

     * @param event Message event.
     */
    fun callCommand(event: MessageReceivedEvent) {
        val content = event.message.content

        if (!content.startsWith(Bot.token)) return

        // Tokenize the message.
        val tokens = content.fastSplit(' ')

        val label = tokens[0].substring(Bot.token.length).toLowerCase()

        val args = tokens.subList(1, tokens.size)

        val note = host.noteOf(event.message)
        val person = host.personHandler.asPerson(event.member)

        val cmd = getCommand(label) ?: return

        if (cmd.clearance.value > person.clearance.value) {
            note.error("Insufficient clearance, requires `${cmd.clearance.value}`.")
            return
        }

        try {
            requests++
            cmd.execute(note, args)

        } catch (e: RuntimeException) {
            note.error("**Exception**: " + e.message)
            e.printStackTrace()
        }

    }
}
