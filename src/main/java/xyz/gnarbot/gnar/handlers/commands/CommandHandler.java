package xyz.gnarbot.gnar.handlers.commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import xyz.gnarbot.gnar.GuildHandler;
import xyz.gnarbot.gnar.handlers.Member;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Arrays;

public class CommandHandler extends CommandRegistry
{
    private final GuildHandler guildHandler;
    
    private int requests = 0;
    
    private String token = "_"; //default token
    
    public CommandHandler(GuildHandler guildHandler)
    {
        this.guildHandler = guildHandler;
    }
    
    public void inheritFrom(CommandDistributor distributor)
    {
        distributor.getSingletonCommands().entrySet().forEach(entry -> registerCommand(entry.getKey(), entry.getValue()));
        distributor.getManagedCommands().forEach(this::registerCommand);
    }
    
    @Deprecated
    public String getToken()
    {
        return token;
    }
    
    @Deprecated
    public void setToken(String token)
    {
        this.token = token;
    }
    
    public void callCommand(MessageReceivedEvent event)
    {
        Member member = guildHandler.getMemberHandler().asMember(event.getAuthor());
        String messageContent = event.getMessage().getContent();
        
        if (messageContent.startsWith(token))
        {
            // Tokenize the message.
            String[] tokens = messageContent.split(" ");
            
            String label = tokens[0];
            String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
            
            Note note = new Note(guildHandler, event.getMessage());
            
            for (String regCommand : getRegistry().keySet())
            {
                if (label.equalsIgnoreCase(token + regCommand))
                {
                    CommandExecutor cmd = getRegistry().get(regCommand);
    
                    if (cmd.getClearance().getValue() > member.getClearance().getValue())
                    {
                        note.reply("You do not have sufficient permission to use this command.");
                        return;
                    }
                    
                    try
                    {
                        cmd.execute(note, label, args);
                        requests++;
                    }
                    catch (RuntimeException e)
                    {
                        note.reply("An exception occurred while executing this command. " + e.toString());
                        e.printStackTrace();
                    }
                    
                    return;
                }
            }
        }
    }
    
    public int getRequests()
    {
        return requests;
    }
}
