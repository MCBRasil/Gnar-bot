package xyz.gnarbot.gnar.handlers.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import xyz.gnarbot.gnar.handlers.members.Member;
import xyz.gnarbot.gnar.handlers.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Arrays;

public class CommandHandler extends CommandRegistry
{
    private final Host host;
    
    private int requests = 0;
    
    private String token = "_"; //default token
    
    public CommandHandler(Host host)
    {
        this.host = host;
    }
    
    /**
     * Extract command classes/instances from CommandDistributor
     * and register it in this handler.
     *
     * @param distributor CommandDistributor instance.
     */
    public void recieveFrom(CommandDistributor distributor)
    {
        distributor.getSingletonCommands().forEach(this::registerCommand);
        distributor.getManagedCommands().forEach(this::registerCommand);
    }
    
    /**
     * Returns the token of the handler.
     * @return The token of the handler.
     */
    public String getToken()
    {
        return token;
    }
    
    @Deprecated
    public void setToken(String token)
    {
        this.token = token;
    }
    
    /**
     * Call the command based on the message content.
     * @param event Message event.
     */
    public void callCommand(MessageReceivedEvent event)
    {
        String messageContent = event.getMessage().getContent();
        
        if (messageContent.startsWith(token))
        {
            // Tokenize the message.
            String[] tokens = messageContent.split(" ");
            
            String label = tokens[0];
            String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
            
            Note note = new Note(host, event.getMessage());
            Member member = host.getMemberHandler().asMember(event.getAuthor());
            
            for (String cmdLabel : getRegistry().keySet())
            {
                if (label.equalsIgnoreCase(token + cmdLabel))
                {
                    CommandExecutor cmd = getRegistry().get(cmdLabel);
    
                    if (cmd.getClearance().getValue() > member.getClearance().getValue())
                    {
                        note.reply("Insufficient permission.");
                        return;
                    }
                    
                    try
                    {
                        cmd.execute(note, label, args);
                        requests++;
                    }
                    catch (RuntimeException e)
                    {
                        note.reply("Error: `" + e.getMessage() + "` occurred.");
                        e.printStackTrace();
                    }
                    
                    return;
                }
            }
        }
    }
    
    /**
     * Return the amount of successful requests on this command handler.
     * @return the amount of successful requests on this command handler.
     */
    public int getRequests()
    {
        return requests;
    }
}
