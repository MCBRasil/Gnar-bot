package xyz.gnarbot.gnar.commands.executors.polls;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import xyz.gnarbot.gnar.Bot;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class YesNoPoll extends Poll {

    private final Message msg;
    private final String question;
    private final int minutes;
    private User startingClient;
    private ScheduledFuture runTask;
    private int pollid;

    public YesNoPoll(Message msg, final String question, int minutes, Bot bot) {
        super(bot);
        this.msg = msg;
        this.question = question;
        this.minutes = minutes;
    }


    @Override
    public void start() {

        System.out.println(msg.getAuthor().getName() + " created a new poll");
        startingClient = msg.getAuthor();
        final Message repliedMessage;

        repliedMessage = msg.respond().embed("Yes or No Poll")
                .description(sb -> {
                    sb.append(":pushpin: *A new poll has been started by* **")
                            .append(startingClient.getName())
                            .append("** `(Poll ID: ")
                            .append(getId())
                            .append(")`\n\n")
                            .append(":paperclip: Question:\n")
                            .append("        ╚ ")
                            .append(question)
                            .append("\n\n")
                            .append(":clock1: Time Left:\n")
                            .append("        ╚ ")
                            .append(minutes)
                            .append(" minute(s) 0 second(s)\n\n")
                            .append("     ")
                            .append("  ")
                            .append(" ╠ ❌ - No  [0 Votes]\n")
                            .append("        ╚ ✅ - Yes [0 Votes]");
                })
                .rest().complete();

        System.out.println(repliedMessage.getId());
        repliedMessage.addReaction("❌");

        runTask = getBot().getScheduler().scheduleAtFixedRate(new Runnable() {
            int minutesInst = minutes;

            int seconds = 0;

            int timetaken = 0;

            @Override
            public void run() {
                System.out.println(timetaken);
                timetaken++;
                if (timetaken == 1) {
                    repliedMessage.addReaction("✅");
                }
                if (minutesInst >= 0) {
                    seconds--;
                    if (seconds <= 0) {
                        seconds = 60;
                        minutesInst--;
                    }
                    if (String.valueOf(seconds).contains("5") || String.valueOf(seconds).contains("0")) {
                        repliedMessage.editMessage(":pushpin: *A new poll has been started by* **" + startingClient
                                .getName() + "** `(Poll ID: " + getId() + ")`\n\n" + ":paperclip: " +
                                "Question:\n" + "        ╚ " + question + "\n\n" + ":clock1: Time Left:\n" + "   " +
                                "     ╚ " + minutesInst + " minute(s) " + seconds + " second(s)\n\n" + ":gem: " +
                                "Votes:\n" + "      " + "  ╠ ❌ - No  [" + (repliedMessage
                                .getReactions()
                                .get(0)
                                .getCount() - 1) + " Votes]\n" + "        ╚ ✅ - Yes [" + (repliedMessage
                                .getReactions()
                                .get(0)
                                .getCount() - 1) + " " + "Votes]").queue();
                    }
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

        getBot().getScheduler().schedule(() ->
        {
            repliedMessage.editMessage(":pushpin: *A new poll has been started by* **" + startingClient.getName() +
                    "**" + " `(Poll ID: " + getId() + ")`\n\n" + ":paperclip: Question:\n" + "        ╚ " +
                    question + "\n\n" + ":clock1: Time Left:\n" + "        ╚ **Voting Over**\n\n" + ":gem: " +
                    "Votes:\n" + "        " + "╠ ❌ - No  [" + (repliedMessage
                    .getReactions()
                    .get(0)
                    .getCount() - 1) + " Votes]\n" + "        ╚ " + "✅ - Yes [" + (repliedMessage.getReactions()
                    .get(0)
                    .getCount() - 1) + " Votes]").queue();

            repliedMessage.respond().text(":exclamation: Poll `#" + getId() + "` by " + startingClient.getName() + " " +
                    "has " + "finished! Check above for the results!");

            startingClient.getPrivateChannel()
                    .sendMessage(":exclamation: Your poll in <#" + msg.getChannel()
                            .getId() + "> has ended! Go check it's results!");

            runTask.cancel(true);
        }, minutes * 60 + 5, TimeUnit.SECONDS);

    }

    public int getId() {
        return pollid;
    }

    public void setId(int id) {
        this.pollid = id;
    }
}
