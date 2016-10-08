package com.gmail.hexragon.gn4rBot.command.general

import com.google.inject.Inject
import xyz.gnarbot.gnar.Host
import xyz.gnarbot.gnar.handlers.Clearance
import xyz.gnarbot.gnar.handlers.commands.Command
import xyz.gnarbot.gnar.handlers.commands.CommandExecutor
import xyz.gnarbot.gnar.textadventure.TextAdventure
import xyz.gnarbot.gnar.utils.GnarQuotes
import xyz.gnarbot.gnar.utils.Note
import java.util.StringJoiner

@Command(aliases = arrayOf("startadventure"), usage = "[command]", description = "Start a text-based aventure!")
class StartAdventureCommand : CommandExecutor()
{
    @Inject lateinit var host : Host
    
    override fun execute(message : Note, label : String, args : Array<out String>)
    {
        TextAdventure(message.author, message.channel, message);

    }
}