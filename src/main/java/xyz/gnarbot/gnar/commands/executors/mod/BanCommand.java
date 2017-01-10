package xyz.gnarbot.gnar.commands.executors.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.utils.PermissionUtil;
import xyz.gnarbot.gnar.commands.handlers.Command;
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor;
import xyz.gnarbot.gnar.members.Clearance;
import xyz.gnarbot.gnar.servers.Host;
import xyz.gnarbot.gnar.utils.Note;

@Command(aliases = "ban", clearance = Clearance.BOT_COMMANDER)
public class BanCommand extends CommandExecutor
{
    @Override
    public void execute(Note note, String label, String[] args)
    {
        Host host = note.getHost();
        
        Member author = note.getAuthor();
        Member target;
        
        try
        {
            try
            { //Checks if they mentioned someone
                target = note.getMentionedUsers().get(0);
            }
            catch (Exception e)
            { //No mention user, maybe checks if they supplied a username?
                target = note.getGuild().getMembersByName(args[0], true).get(0);
            }
        }
        catch (Exception e)
        {
            note.error("User is not on this server.");
            return;
        }
        
        if (target != null)
        {
            if (PermissionUtil.checkPermission(note.getTextChannel(), author, Permission.BAN_MEMBERS))
            {
                if (PermissionUtil.canInteract(author, target))
                {
                    if (host.ban(target.getUser().getId()))
                    {
                        note.info(target.getEffectiveName() + " has been banned.");
                    }
                    else
                    {
                        note.error("Gnar does not have permission to ban.");
                    }
                }
                else
                {
                    note.error("Sorry, you can not ban that user as he is above you.");
                }
            }
            else
            {
                note.error("You do not have permission to ban.");
            }
        }
        else
        {
            note.error("Could not find user, maybe you made a typo?");
        }
    }
}