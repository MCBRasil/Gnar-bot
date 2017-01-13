package xyz.gnarbot.gnar.commands.handlers;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.members.User;
import xyz.gnarbot.gnar.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

import java.util.Arrays;

public class CommandHandler extends CommandRegistry
{
    private final Host host;
    
    private int requests = 0;
    
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
    public void recieveFrom(CommandTable distributor)
    {
        distributor.getSingletonCommands().forEach(this::registerCommand);
        distributor.getManagedCommands().forEach(this::registerCommand);
    }
    
    /**
     * Call the command based on the message content.
     *
     * @param event Message event.
     */
    public void callCommand(MessageReceivedEvent event)
    {
        String messageContent = event.getMessage().getContent();
        
        if (messageContent.startsWith(Bot.getToken()))
        {
            // Tokenize the message.
            String[] tokens = messageContent.split(" ");
            
            String label = tokens[0];
            String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
            
            Note note = host.noteOf(event.getMessage());
            User user = host.getUserHandler().asUser(event.getMember());
            
            for (String cmdLabel : getRegistry().keySet())
            {
                if (label.equalsIgnoreCase(Bot.getToken() + cmdLabel))
                {
                    CommandExecutor cmd = getRegistry().get(cmdLabel);
                    
                    if (cmd.getClearance().getValue() > user.getClearance().getValue())
                    {
                        note.error("Insufficient permission.");
                        return;
                    }
                    
                    try
                    {
                        requests++;
                        cmd.execute(note, label, args);
                    }
                    catch (RuntimeException e)
                    {
                        note.error("**Exception**:" + e.getMessage());
                        e.printStackTrace();
                    }
                    
                    return;
                }
            }
        }
    }
    
    /**
     * Return the amount of successful requests on this command handler.
     *
     * @return the amount of successful requests on this command handler.
     */
    public int getRequests()
    {
        return requests;
    }
}
