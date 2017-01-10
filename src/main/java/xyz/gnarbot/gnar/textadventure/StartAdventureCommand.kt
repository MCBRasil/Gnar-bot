package xyz.gnarbot.gnar.textadventure

import xyz.gnarbot.gnar.commands.handlers.*
import xyz.gnarbot.gnar.utils.Note

@Command(aliases = arrayOf("startadventure"), usage = "[command]", description = "Start a text-based aventure!")
class StartAdventureCommand : CommandExecutor()
{
    override fun execute(note : Note, label : String, args : Array<out String>)
    {
        Adventure.getAdventure(note.author, note)
        note.deleteMessage()
    }
}