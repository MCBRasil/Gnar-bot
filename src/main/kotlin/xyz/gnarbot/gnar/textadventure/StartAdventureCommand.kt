package xyz.gnarbot.gnar.textadventure

import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("startadventure"), usage = "[command]", description = "Start a text-based aventure!")
class StartAdventureCommand : CommandExecutor() {
    override fun execute(note: Note, args: List<String>) {
        Adventure.getAdventure(note.author, note)
        note.optDelete()
    }
}