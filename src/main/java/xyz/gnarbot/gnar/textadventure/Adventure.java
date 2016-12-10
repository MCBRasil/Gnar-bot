package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
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
				"\n ➜ *Example: `_adventure " + u.getName() + " the Great`*");
	}

	public void logAction(String action) {
		actionList.add("(" + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + ") " + action);
		System.out.println("(" + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + ") " + action);
	}

	public String getPlayerIcon(){
		if (gender.equalsIgnoreCase("selecting")) {
			return "http://i.imgur.com/HxWJti2.png";
		}else{
			if (gender.equalsIgnoreCase("male")){
				return "http://i.imgur.com/0Uh68PW.png";
			}else if (gender.equalsIgnoreCase("female")){
				return "http://i.imgur.com/tgkBMjv.png";
			}
			return null;
		}
	}

	public void sendMessage(Note n, String message) {
		this.lastMessage = message;
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("**" + n.getAuthor().getName() + "'s Adventure**").addBlankField(true).setDescription(message)
				.setColor(new Color(39, 255, 9));
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

	public void sendInformativeMessage(Note n, String message) {
		lastSentMessage = n.reply("\n" + message);
	}

	public void sendLastMessage(Note n, String extra) {

		EmbedBuilder eb = new EmbedBuilder();
		Random r = new Random();
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

	private String lastResponse;

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
			sendMessage(n, "*A new adventure begins! This is the story of...* ***`" + heroName + "`!***\n\nWait a moment... Are you a ");
			state = STATE.WAITING;
			stateRelation = "selectGender";
			logAction("Decided that you would call yourself '" + getHeroName() + "'");
			areasFound++;
		} else if (stateRelation.equalsIgnoreCase("selectGender") && this.state == STATE.WAITING){
			startArea = this.grid.getAreaAtLocation(this.grid.getCurrentX(), this.grid.getCurrentY());
			currentArea = startArea;
			currentArea.discover();
			String[] actions = new String[]{"walking into", "running towards", "swimming towards", "teleported to", "suddenly in"};
		} else {
			if (state == STATE.WAITING && stateRelation.equalsIgnoreCase("move")){
				lastResponse = response;
				if (response.equalsIgnoreCase("up") || response.equalsIgnoreCase("down") || response.equalsIgnoreCase("left") || response.equalsIgnoreCase("right") ||
						response.equalsIgnoreCase("north") || response.equalsIgnoreCase("south") || response.equalsIgnoreCase("east") || response.equalsIgnoreCase("west")){
                    /*DIRECTION dir = DIRECTION.getFromString(response.toLowerCase());
                    if (dir != null && currentArea.canMoveInDir(dir)){
                        if (currentArea.getAreaFromDir(dir) == null){
                            currentArea.setAreaInDir(dir, new Area(this, dir, currentArea));
                        }
                        currentArea = currentArea.getAreaFromDir(dir);
                        if (currentArea.moveToThis()){
                            areasFound++;
                            if (!fromEvent) {
                                logAction("Moved " + response + " into a new location! It's a " + currentArea.getType().getName());
                            }
                        }else{
                            if (!fromEvent) {
                                logAction("Moved " + response + " back into a " + currentArea.getType().getName());
                            }
                        }
	                    if (!currentArea.hasEvent() || currentArea.hasCompletedEvent() || fromEvent) {
		                    sendMessage(n, getNewLocationText(currentArea, currentArea.getType(), "walking `" + response.toUpperCase(Locale.ENGLISH) + "` to")); // First Location allows for any direction of movement.
	                    }
	                    else{
                            runEvent(n, currentArea);
                        }
                    }else{
	                    sendInformativeMessage(n, "You can't move in that direction! There's something blocking your path!");
                    }*/
					return;
				}
			}
			sendInformativeMessage(n, "I'm unsure of what you meant by `" + response + "`. Type `_adventure help` to bring up the Help Menu."); // Placeholder until I add the moving system.
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
