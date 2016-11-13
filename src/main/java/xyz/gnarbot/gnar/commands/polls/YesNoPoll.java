package xyz.gnarbot.gnar.commands.polls;

import net.dv8tion.jda.client.managers.EmoteManager;
import net.dv8tion.jda.core.entities.Emote;
import sun.nio.cs.UnicodeEncoder;
import sun.text.normalizer.UnicodeMatcher;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.handlers.members.Member;
import xyz.gnarbot.gnar.utils.Note;
import xyz.gnarbot.gnar.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Created by zacha on 11/5/2016.
 */
public class YesNoPoll extends Poll{

	private Member startingUser;
	private ScheduledFuture runTask;

	private int pollid;

	private Note n;
	private String question;
	private int minutes;

	public YesNoPoll(Note n, final String question, int minutes){
		this.n = n;
		this.question = question;
		this.minutes = minutes;
	}

	@Override
	public void startPoll() {

		System.out.println(n.getAuthor().getName() + " created a new poll");
		startingUser = n.getAuthor();
		final Note repliedMessage = n.replyRaw(":pushpin: *A new poll has been started by* **" + startingUser.getName() + "** `(Poll ID: " + getPollid() + ")`\n\n" +
				":paperclip: Question:\n" +
				"        ╚ " + question + "\n\n"+
				":clock1: Time Left:\n" +
				"        ╚ " + minutes + " minute(s) 0 second(s)\n\n" +
				":gem: Options:\n" +
				"        ╠ :x: - No\n" +
				"        ╚ :white_check_mark: - Yes");
		System.out.println(repliedMessage.getId());
		Utils.sendReactionAutoEncode(repliedMessage, "❌");
		runTask = Bot.INSTANCE.getScheduler().scheduleAtFixedRate(new Runnable() {
			int minutesInst = minutes;
			int seconds = 0;
			int timetaken = 0;
			@Override
			public void run() {
				timetaken++;
				if (timetaken == 1){
					Utils.sendReactionAutoEncode(repliedMessage, "✅");
				}
				if (minutesInst >= 0){
					seconds--;
					if (seconds <= 0){
						seconds = 60;
						minutesInst--;
					}
					if (seconds == 0 || seconds == 15 || seconds == 30 || seconds == 45) {
						repliedMessage.editMessage(":pushpin: *A new poll has been started by* **" + startingUser.getName() + "** `(Poll ID: " + getPollid() + ")`\n\n" +
								":paperclip: Question:\n" +
								"        ╚ " + question + "\n\n" +
								":clock1: Time Left:\n" +
								"        ╚ " + minutesInst + " minute(s) " + seconds + " second(s)\n\n" +
								":gem: Options:\n" +
								"        ╠ :x: - No\n" +
								"        ╚ :white_check_mark: - Yes").queue();
					}
				}
			}
		}, 1, 1, TimeUnit.SECONDS);
		Bot.INSTANCE.getScheduler().schedule(new Runnable() {
			@Override
			public void run() {
				repliedMessage.editMessage(":pushpin: *A new poll has been started by* **" + startingUser.getName() + "** `(Poll ID: " + getPollid() + ")`\n\n" +
						":paperclip: Question:\n" +
						"        ╚ " + question + "\n\n" +
						":clock1: Time Left:\n" +
						"        ╚ **Voting Over**\n\n" +
						":gem: Options:\n" +
						"        ╠ :x: - No\n" +
						"        ╚ :white_check_mark: - Yes").queue();;
				repliedMessage.replyRaw(":exclamation: Poll `#" + getPollid() +"` by " + startingUser.getName() + " has finished! Check above for the results!");
				startingUser.getPrivateChannel().sendMessage(":exclamation: Your poll in <#" + n.getChannel().getId() + "> has ended! Go check it's results!");
				runTask.cancel(true);
				return;
			}
		}, minutes*60+5, TimeUnit.SECONDS);
	}

	public int getPollid() {
		return pollid;
	}

	public void setPollid(int pollid) {
		this.pollid = pollid;
	}
}
