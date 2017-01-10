package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.utils.PermissionUtil;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Clearance;
import xyz.gnarbot.gnar.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "kick", clearance = Clearance.BOT_COMMANDER)
public class KickCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        Host host = note.getHost();
        
        Member author = note.getAuthor();
        Member target;
        
        try
        { //Checks if they mentioned someone
            target = note.getMentionedUsers().get(0);
        }
        catch (Exception e)
        { //No mention user, maybe checks if they supplied a username?
            target = note.getGuild().getMembersByName(args[0], true).get(0);
        }
        
        if (target != null)
        {
            if (PermissionUtil.checkPermission(note.getTextChannel(), author, Permission.KICK_MEMBERS))
            {
                if (PermissionUtil.canInteract(author, target))
                {
                    if (host.kick(target))
                    {
                        note.info(target.getEffectiveName() + " has been kicked.");
                    }
                    else
                    {
                        note.error("GNAR does not have permission to kick.");
                    }
                }
                else
                {
                    note.error("Sorry, you can not kick that user as he is above you.");
                }
            }
            else
            {
                note.error("You do not have permission to kick.");
            }
        }
        else
        {
            note.error("Could not find user, maybe you made a typo?");
        }
    }
}
