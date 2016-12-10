package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import xyz.gnarbot.gnar.textadventure.enums.DIRECTION;
import xyz.gnarbot.gnar.textadventure.events.FirstBagEvent;
import xyz.gnarbot.gnar.utils.Note;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zacha on 12/10/2016.
 */
public class Adventure {


	public Adventure(User u, Note note) {

		adventures.put(u, this);
		this.user = u;
		this.starttime = System.currentTimeMillis();
		this.gameID = UUID.randomUUID();
		this.random = new Random();
		this.random.setSeed(this.starttime);
		this.grid = new AdventureGrid(this);
		state = STATE.WAITING_FOR_NAME;
		stateRelation = "waitname";
		logAction("Started your adventure...");
		System.out.println("Started new Text Adventure for " + u.getName() + " (ID: " + gameID.toString() + ")");
		sendMessage(note, "***A new adventure begins... This is the story of... `_________`***\n" +
				"\n" +
				"\n                        :warning:      **Response Required!**      :warning:" +
				"\n  :bulb: :bulb: :bulb:      **What is your name, hero?**      :bulb: :bulb: :bulb:   " +
				"\n ➜ *To answer dialog options, use the `_adventure` command!*" +
				"\n ➜ *Example: `_adventure " + u.getName() + " the Great`*", Color.WHITE);
	}

	public void logAction(String action) {
		actionList.add("(" + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + ") " + action);
		System.out.println("(" + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + ") " + action);
	}

	public String getPlayerIcon(){
		if (gender.equalsIgnoreCase("selecting")) {
			return "http://i.imgur.com/HxWJti2.png";
		}else{
			if (gender.equalsIgnoreCase("boy")){
				return "http://i.imgur.com/0Uh68PW.png";
			}else if (gender.equalsIgnoreCase("girl")){
				return "http://i.imgur.com/tgkBMjv.png";
			}
			return null;
		}
	}

	public void sendMessage(Note n, String message) {
		this.lastMessage = message;
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("**" + n.getAuthor().getName() + "'s Adventure**").addBlankField(true).setDescription(message)
				.setColor(getDefaultMessageColor());
		if (getPlayerIcon() != null){
			eb.setThumbnail(getPlayerIcon());
		}
		MessageEmbed embed = eb.build();
		MessageBuilder mb = new MessageBuilder();
		mb.setEmbed(embed);
		Message m = mb.build();
		n.getChannel().sendMessage(m).queue();
		lastSentMessage = m;
	}

	public void sendMessage(Note n, String message, Color color) {
		this.lastMessage = message;
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("**" + n.getAuthor().getName() + "'s Adventure**").addBlankField(true).setDescription(message)
				.setColor(color);
		if (getPlayerIcon() != null){
			eb.setThumbnail(getPlayerIcon());
		}
		MessageEmbed embed = eb.build();
		MessageBuilder mb = new MessageBuilder();
		mb.setEmbed(embed);
		Message m = mb.build();
		n.getChannel().sendMessage(m).queue();
		lastSentMessage = m;
	}

	public void sendMessage(Note n, String message, String url) {
		this.lastMessage = message;
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("**" + n.getAuthor().getName() + "'s Adventure**").addBlankField(true).setDescription(message)
				.setColor(getDefaultMessageColor());
		eb.setThumbnail(url);
		MessageEmbed embed = eb.build();
		MessageBuilder mb = new MessageBuilder();
		mb.setEmbed(embed);
		Message m = mb.build();
		n.getChannel().sendMessage(m).queue();
		lastSentMessage = m;
	}

	public void sendMessage(Note n, String message, String url, Color color) {
		this.lastMessage = message;
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("**" + n.getAuthor().getName() + "'s Adventure**").addBlankField(true).setDescription(message)
				.setColor(color);
			eb.setThumbnail(url);
		MessageEmbed embed = eb.build();
		MessageBuilder mb = new MessageBuilder();
		mb.setEmbed(embed);
		Message m = mb.build();
		n.getChannel().sendMessage(m).queue();
		lastSentMessage = m;
	}

	public void sendInformativeMessage(Note n, String message) {
		lastSentMessage = n.reply("\n" + message);
	}

	public void sendLastMessage(Note n, String extra) {

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("**" + n.getAuthor().getName() + "'s Adventure**").addBlankField(true).setDescription(lastMessage).setThumbnail(getPlayerIcon())
				.setColor(new Color(39, 255, 9));
		MessageEmbed embed = eb.build();
		MessageBuilder mb = new MessageBuilder();
		mb.setEmbed(embed);
		Message m = mb.build();
		n.getChannel().sendMessage(m).queue();
	}


	public void getResponseFromEvent(Event e, String response){
		if (e instanceof FirstBagEvent && response.equalsIgnoreCase("completed")){
			this.inventory = new Inventory(9);
		}
	}


	public void parseResponse(Note n, String response, boolean fromEvent) {
		System.out.println("Got response for " + user.getName() + "'s adventure: \n" + response);
		if (state == STATE.WAITING && stateRelation.equalsIgnoreCase("EVENTRESPONSE")){
			this.currentEvent.parseResponse(this, n, response);
			if (this.currentEvent.hasCompletedEvent()){
				this.stateRelation = "move";
			}
			return;
		}
		if (stateRelation.equalsIgnoreCase("waitname") && this.state == STATE.WAITING_FOR_NAME) {
			setHeroName(response);
			this.grid.beginBuild();
			gender = "selecting";
			sendMessage(n, "*A new adventure begins! This is the story of...* ***`" + heroName + "`!***\n\nWait a moment... Are you a **BOY** or a **GIRL**?", Color.WHITE);
			state = STATE.WAITING;
			stateRelation = "selectGender";
			logAction("Decided that you would call yourself '" + getHeroName() + "'");
			areasFound++;
		} else if (stateRelation.equalsIgnoreCase("selectGender") && this.state == STATE.WAITING){
			if (response.equalsIgnoreCase("boy") || response.equalsIgnoreCase("girl")){
				startArea = this.grid.getAreaAtLocation(this.grid.getCurrentX(), this.grid.getCurrentY());
				setGender(response);
				state = STATE.WAITING;
				stateRelation = "move";
				currentArea = startArea;
				getGrid().getCurrentArea().discover();
				sendMessage(n, "Ah! So you're a " + response + "! Fantastic! Let's get you started on your very own adventure!\nThe world is yours to claim! Go out and claim it!\n\n ➜ *To move, use the `_adventure` command!*\n Example: *`_adventure up`* will try to move you up\n ➜ *To view your map, use the `_adventure map` command!*");
			}else{
				sendMessage(n, "I'm unsure of what you meant by `" + response + "`. Type `_adventure help` to bring up the Help Menu.", Color.RED); // Placeholder until I add the moving system.
			}
		} else {
			if (state == STATE.WAITING && stateRelation.equalsIgnoreCase("move")){
				lastResponse = response;
				if (response.equalsIgnoreCase("up") || response.equalsIgnoreCase("down") || response.equalsIgnoreCase("left") || response.equalsIgnoreCase("right") ||
				response.equalsIgnoreCase("north") || response.equalsIgnoreCase("south") || response.equalsIgnoreCase("east") || response.equalsIgnoreCase("west")) {
					if (!getGrid().moveInDirection(DIRECTION.getFromString(response))){
						sendMessage(n, "Oops! There's something blocking your way!", "http://i.imgur.com/R9gfp56.png", Color.RED);
					}else{
						if (getGrid().getCurrentArea().isNewLocation()){
							areasFound++;
						}
						getGrid().getCurrentArea().discover();
						getGrid().getCurrentArea().moveToHere();
						sendMessage(n, "You continue onwards, towards a " + getGrid().getCurrentArea().getType().getName(), getGrid().getCurrentArea().getType().getUrl(), Color.RED);
						if (getGrid().getCurrentArea().getRelatedEvent() != null && !getGrid().getCurrentArea().hasCompletedEvent()){
							getGrid().getCurrentArea().getRelatedEvent().runEvent(this, n);
						}
					}
					return;
				}
			}
			sendMessage(n, "I'm unsure of what you meant by `" + response + "`. Type `_adventure help` to bring up the Help Menu.", Color.RED); // Placeholder until I add the moving system.
		}
	}


	/* -------------------------------------------------- */

	private static HashMap<User, Adventure> adventures = new HashMap<User, Adventure>();
	private static int lastID = 0;

	public static Adventure getAdventure(User u, Note n) {
		System.out.println(u.getName() + " requested a Text Adventure");
		if (adventures.containsKey(u)) {
			return adventures.get(u);
		} else {
			return new Adventure(u, n);
		}
	}

	private Random random;

	protected enum STATE {
		WORKING, WAITING, INTERACTING, RESPONSE_REQUIRED, WAITING_FOR_NAME;
	}

	private User user;
	private UUID gameID;
	private Color defaultMessageColor = new Color(39, 255, 9);
	private String lastResponse;
	private Long starttime;
	private ArrayList<Area> areas = new ArrayList<>();
	private ArrayList<String> actionList = new ArrayList<>();
	private String heroName;
	private Adventure.STATE state;
	private String stateRelation;
	private String lastMessage;
	private Message lastSentMessage;
	private Inventory inventory = null;
	private Area currentArea, startArea;
	private int areasFound = 0;
	private Event currentEvent;
	private AdventureGrid grid;
	private String gender = "";

	public Color getDefaultMessageColor() {
		return defaultMessageColor;
	}

	public String getLastResponse() {
		return lastResponse;
	}

	public static int getLastID() {
		return lastID;
	}

	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public void setState(STATE state) {
		this.state = state;
	}

	public void setStateRelation(String stateRelation) {
		this.stateRelation = stateRelation;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Random getRandom() {
		return random;
	}

	public User getUser() {
		return user;
	}

	public UUID getGameID() {
		return gameID;
	}

	public Long getStarttime() {
		return starttime;
	}

	public ArrayList<Area> getAreas() {
		return areas;
	}

	public ArrayList<String> getActionList() {
		return actionList;
	}

	public String getHeroName() {
		return heroName;
	}

	public STATE getState() {
		return state;
	}

	public String getStateRelation() {
		return stateRelation;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public Message getLastSentMessage() {
		return lastSentMessage;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Area getCurrentArea() {
		return currentArea;
	}

	public Area getStartArea() {
		return startArea;
	}

	public int getAreasFound() {
		return areasFound;
	}

	public Event getCurrentEvent() {
		return currentEvent;
	}

	public AdventureGrid getGrid() {
		return grid;
	}

	public String getGender() {
		return gender;
	}


}
