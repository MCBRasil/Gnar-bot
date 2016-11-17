package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import xyz.gnarbot.gnar.textadventure.enums.DIRECTION;
import xyz.gnarbot.gnar.textadventure.enums.LOCATION;
import xyz.gnarbot.gnar.textadventure.events.FirstBagEvent;
import xyz.gnarbot.gnar.textadventure.events.FirstSwordEvent;
import xyz.gnarbot.gnar.textadventure.events.FirstSwordLewdEvent;
import xyz.gnarbot.gnar.utils.Note;

import java.text.SimpleDateFormat;
import java.util.*;

public class TextAdventure {

    private static HashMap<User, TextAdventure> adventures = new HashMap<User, TextAdventure>();
    private static int lastID = 0;

    public static TextAdventure getAdventure(User u, Note n) {
        System.out.println(u.getName() + " requested a Text Adventure");
        if (adventures.containsKey(u)) {
            return adventures.get(u);
        } else {
            return new TextAdventure(u, n);
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
    private STATE state;
    private String stateRelation;

    private String lastMessage;
    private Message lastSentMessage;

    private Inventory inventory = null;

    private Area currentArea, startArea;

    private int areasFound = 0;

    private Event currentEvent;

    private AdventureGrid grid;

    public AdventureGrid getGrid() {
        return grid;
    }

    public UUID getGameID() {
        return gameID;
    }

    public Random getRandom() {
        return random;
    }

    public User getUser() {
        return user;
    }

    public Long getStarttime() {
        return starttime;
    }

    public STATE getState() {
        return state;
    }

    public String getStateRelation() {
        return stateRelation;
    }

    public int getAreasFound() {
        return areasFound;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public TextAdventure(User u, Note note) {

        adventures.put(u, this);
        this.user = u;
        this.starttime = System.currentTimeMillis();
        this.gameID = UUID.randomUUID();
        this.random = new Random();
        this.random.setSeed(this.starttime);
        this.grid = new AdventureGrid(this);
        this.grid.beginBuild();
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

    public void sendMessage(Note n, String message) {
        this.lastMessage = message;
        lastSentMessage = n.reply("\n" + message);
    }

    public void sendInformativeMessage(Note n, String message) {
        lastSentMessage = n.reply("\n" + message);
    }

    public void sendLastMessage(Note n, String extra) {
        n.reply(extra + "\n" + lastMessage);
    }

    private String getNewLocationText(Area originArea, LOCATION locationType, String action) {

        String r = String.format("*We find our hero, %s %s a...*\n        **%s** \n** Available Directions: **\n       <north>\n" +
                "<west>       <east>\n" +
                "       <south>" +
                "\n :bulb: `Use the _adventure command to go a certain direction! Example: _adventure North`", this.heroName, action, locationType.getName());
        /*if (originArea.canMoveNorth()) {
            r = r.replaceAll("<north>", ":arrow_up:");
        } else {
            r = r.replaceAll("<north>", ":no_pedestrians:");
        }
        if (originArea.canMoveSouth()) {
            r = r.replaceAll("<south>", ":arrow_down:");
        } else {
            r = r.replaceAll("<south>", ":no_pedestrians:");
        }
        if (originArea.canMoveEast()) {
            r = r.replaceAll("<east>", ":arrow_right:");
        } else {
            r = r.replaceAll("<east>", ":no_pedestrians:");
        }
        if (originArea.canMoveWest()) {
            r = r.replaceAll("<west>", ":arrow_left:");
        } else {
            r = r.replaceAll("<west>", ":no_pedestrians:");
        }*/

        return r;
    }

    private void runEvent(Note n, Area area) {
        area.getRelatedEvent().runEvent(this, n);
        this.state = STATE.WAITING;
        this.stateRelation = "EVENTRESPONSE";
        this.currentEvent = area.getRelatedEvent();
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
                sendMessage(n, getNewLocationText(currentArea, currentArea.getType(), "walking `" + lastResponse.toUpperCase(Locale.ENGLISH) + "` to"));
            }
            return;
        }
        if (stateRelation.equalsIgnoreCase("waitname") && this.state == STATE.WAITING_FOR_NAME) {
            setHeroName(response);
	        String[] actions = new String[]{"walking into", "running towards", "swimming towards", "teleported to", "suddenly in"};
            lastSentMessage.editMessage("***A new adventure begins... This is the story of... `" + heroName + "`***");
            sendMessage(n, "*A new adventure begins! This is the story of...* ***`" + heroName + "`!***");
            state = STATE.WAITING;
            stateRelation = "move";
            startArea = newArea(DIRECTION.FIRSTMOVE);
            currentArea = startArea;
            currentArea.discover();
            logAction("Decided that you would call yourself '" + getHeroName() + "'");
            sendMessage(n, getNewLocationText(currentArea, startArea.getType(), actions[random.nextInt(actions.length)])); // First Location allows for any direction of movement.
            areasFound++;
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

    public void setHeroName(String heroName) {
        this.heroName = heroName.replaceAll("`", "").replaceAll("_", "").replaceAll("\\*", "");
    }

    public String getHeroName() {
        return heroName;
    }

    public ArrayList<String> getActionList() {
        return actionList;
    }

    private Area newArea(DIRECTION direction) {
        Area a = new Area(this, direction);
        areas.add(a);
        return a;
    }
}
