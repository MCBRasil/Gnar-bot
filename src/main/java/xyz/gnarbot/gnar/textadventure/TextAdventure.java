package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import xyz.gnarbot.gnar.textadventure.events.FirstBagEvent;
import xyz.gnarbot.gnar.utils.Note;

import java.text.SimpleDateFormat;
import java.util.*;

public class TextAdventure {

    private static HashMap<User, TextAdventure> adventures = new HashMap<User, TextAdventure>();
    private static int lastID = 0;

    public static TextAdventure getAdventure(User u, Note n) {
        System.out.println(u.getUsername() + " requested a Text Adventure");
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

    protected enum LOCATION {
        LAKE("Lake"), RIVER("River"), FOREST("Forest"), CLEARING("Clearing"), PLAINS("Plains"),
        MOUNTAIN("Mountain"), HILL("Hill"), HOUSE("House"), EVIL_HOUSE("House?"), BEACH("Beach"),
        DESERT("Desert"), DEAD_END("Dead End");
        private String id;

        public String getName() {
            return this.id;
        }

        LOCATION(String id) {
            this.id = id;
        }
    }

    protected enum DIRECTION {
        NORTH("north", "up"), SOUTH("south", "down"), EAST("east", "right"), WEST("west", "left"), FIRSTMOVE("firstmove", "firstmove");
        private String name;
        private String alt;

        public String getName() {
            return name;
        }

        public String getAlt() {
            return alt;
        }

        public static DIRECTION getFromString(String dir) {
            for (DIRECTION d : values()) {
                if (d.getName().equalsIgnoreCase(dir) || d.getAlt().equalsIgnoreCase(dir)) {
                    return d;
                }
            }
            return null;
        }

        DIRECTION(String name, String alt) {
            this.name = name;
            this.alt = alt;
        }
    }

    protected enum ENEMY {
        BOAR, PIG, CHICKEN, MONSTER, GHOST, GHOUL;
    }

    private class Area {
        private LOCATION locationType;
        private int id;
        private boolean newLocation = true;

        private Area areaNorth, areaSouth, areaEast, areaWest, connectedArea;
        private boolean canMoveNorth = true, canMoveSouth = true, canMoveEast = true, canMoveWest = true, hasEvent = false;

        private Event relatedEvent;

        public Event getRelatedEvent() {
            return relatedEvent;
        }

        public boolean isNewLocation() {
            return newLocation;
        }

        public boolean moveToThis(){
            if (this.newLocation) {
                this.newLocation = false;
                System.out.println("New location. ID: " + areasFound);
                if (areasFound == 4){
                    hasEvent = true;
                    relatedEvent = new FirstBagEvent();
                    System.out.println("Created an Event!");
                }
                return true;
            }else{
                return false;
            }
        }

        public Area getAreaFromDir(DIRECTION dir){
            if (dir == DIRECTION.NORTH){
                return getAreaNorth();
            }
            if (dir == DIRECTION.SOUTH){
                return getAreaSouth();
            }
            if (dir == DIRECTION.EAST){
                return getAreaEast();
            }
            if (dir == DIRECTION.WEST){
                return getAreaWest();
            }
            return null;
        }

        public boolean canMoveInDir(DIRECTION dir){
            if (dir == DIRECTION.NORTH){
                return canMoveNorth();
            }
            if (dir == DIRECTION.SOUTH){
                return canMoveSouth();
            }
            if (dir == DIRECTION.EAST){
                return canMoveEast();
            }
            if (dir == DIRECTION.WEST){
                return canMoveWest();
            }
            return false;
        }

        public boolean setAreaInDir(DIRECTION dir, Area area){
            if (dir == DIRECTION.NORTH){
                setAreaNorth(area);
                return true;
            }
            if (dir == DIRECTION.SOUTH){
                setAreaSouth(area);
                return true;
            }
            if (dir == DIRECTION.EAST){
                setAreaEast(area);
                return true;
            }
            if (dir == DIRECTION.WEST){
                setAreaWest(area);
                return true;
            }
            return false;
        }

	    public boolean hasEvent() {
		    return hasEvent;
	    }

	    public boolean hasCompletedEvent() {
		    return (relatedEvent != null) ? relatedEvent.hasCompletedEvent() : true;
	    }

	    public Area getAreaNorth() {
            return areaNorth;
        }

        public void setAreaNorth(Area areaNorth) {
            this.areaNorth = areaNorth;
        }

        public Area getAreaSouth() {
            return areaSouth;
        }

        public void setAreaSouth(Area areaSouth) {
            this.areaSouth = areaSouth;
        }

        public Area getAreaEast() {
            return areaEast;
        }

        public void setAreaEast(Area areaEast) {
            this.areaEast = areaEast;
        }

        public Area getAreaWest() {
            return areaWest;
        }

        public void setAreaWest(Area areaWest) {
            this.areaWest = areaWest;
        }

        public Area getConnectedArea() {
            return connectedArea;
        }

        public void setConnectedArea(Area connectedArea) {
            this.connectedArea = connectedArea;
        }

        private DIRECTION prevDirect;

        public Area(LOCATION location, DIRECTION prevDirection) {
            this.locationType = location;
            this.prevDirect = prevDirection;
            initate();
        }

        public Area(DIRECTION prevDirection, Area previousArea) {
	        if (random.nextInt() * 100 > 90) {
		        this.locationType = LOCATION.values()[random.nextInt(LOCATION.values().length)];
	        }else{
		        this.locationType = previousArea.locationType;
	        }
            this.prevDirect = prevDirection;
            this.connectedArea = previousArea;
            if (prevDirection == DIRECTION.NORTH) {
                setAreaSouth(previousArea);
            }
            if (prevDirection == DIRECTION.SOUTH) {
                setAreaNorth(previousArea);
            }
            if (prevDirection == DIRECTION.EAST) {
                setAreaWest(previousArea);
            }
            if (prevDirection == DIRECTION.WEST) {
                setAreaEast(previousArea);
            }

            initate();
        }

        public Area(LOCATION locationType, DIRECTION prevDirection, Area previousArea) {
            this.locationType = locationType;
            this.prevDirect = prevDirection;
            this.connectedArea = previousArea;
            if (prevDirection == DIRECTION.NORTH) {
                setAreaSouth(previousArea);
            }
            if (prevDirection == DIRECTION.SOUTH) {
                setAreaNorth(previousArea);
            }
            if (prevDirection == DIRECTION.EAST) {
                setAreaWest(previousArea);
            }
            if (prevDirection == DIRECTION.WEST) {
                setAreaEast(previousArea);
            }

            initate();
        }

        public Area(DIRECTION prevDirection) {
            this.locationType = LOCATION.values()[random.nextInt(LOCATION.values().length)];
            this.prevDirect = prevDirection;
            initate();
        }

        private void initate() {
            if (prevDirect == DIRECTION.FIRSTMOVE) {
                setAreaNorth(new Area(DIRECTION.NORTH, this));
                setAreaEast(new Area(DIRECTION.EAST, this));
                setAreaSouth(new Area(DIRECTION.SOUTH, this));
                setAreaWest(new Area(DIRECTION.WEST, this));
                if (getType() == LOCATION.DEAD_END){
                    this.locationType = LOCATION.CLEARING;
                }
            }else{
	            if (random.nextInt() * 100 > 90){
		            canMoveNorth = false;
	            }
	            if (random.nextInt() * 100 > 90){
		            canMoveEast = false;
	            }
	            if (random.nextInt() * 100 > 90){
		            canMoveWest = false;
	            }
	            if (random.nextInt() * 100 > 90){
		            canMoveSouth = false;
	            }

	            if (locationType == LOCATION.DEAD_END){
		            canMoveSouth = false;
		            canMoveNorth = false;
		            canMoveEast = false;
		            canMoveWest = false;
	            }

	            if (prevDirect == DIRECTION.NORTH){
		            canMoveSouth = true;
	            }
	            if (prevDirect == DIRECTION.EAST){
		            canMoveWest = true;
	            }
	            if (prevDirect == DIRECTION.WEST){
		            canMoveEast = true;
	            }
	            if (prevDirect == DIRECTION.SOUTH){
		            canMoveNorth = true;
	            }
            }
        }

        public boolean canMoveNorth() {
            return canMoveNorth;
        }

        public boolean canMoveSouth() {
            return canMoveSouth;
        }

        public boolean canMoveEast() {
            return canMoveEast;
        }

        public boolean canMoveWest() {
            return canMoveWest;
        }

        public LOCATION getType() {
            return this.locationType;
        }
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
        state = STATE.WAITING_FOR_NAME;
        stateRelation = "waitname";
        logAction("Started your adventure...");
        System.out.println("Started new Text Adventure for " + u.getUsername() + " (ID: " + gameID.toString() + ")");
        sendMessage(note, "***A new adventure begins... This is the story of... `_________`***\n" +
                "\n" +
                "\n                        :warning:      **Response Required!**      :warning:" +
                "\n  :bulb: :bulb: :bulb:      **What is your name, hero?**      :bulb: :bulb: :bulb:   " +
                "\n ➜ *To answer dialog options, use the `_adventure` command!*" +
                "\n ➜ *Example: `_adventure " + u.getUsername() + " the Great`*");
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
        if (originArea.canMoveNorth()) {
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
        }

        return r;
    }

    private void runEvent(Note n, Area area, LOCATION locationType, String action) {
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
        System.out.println("Got response for " + user.getUsername() + "'s adventure: \n" + response);
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
            lastSentMessage.updateMessage("***A new adventure begins... This is the story of... `" + heroName + "`***");
            sendMessage(n, "*A new adventure begins! This is the story of...* ***`" + heroName + "`!***");
            state = STATE.WAITING;
            stateRelation = "move";
            startArea = newArea(DIRECTION.FIRSTMOVE);
            currentArea = startArea;
            currentArea.moveToThis();
            logAction("Decided that you would call yourself '" + getHeroName() + "'");
            sendMessage(n, getNewLocationText(currentArea, startArea.getType(), actions[random.nextInt(actions.length)])); // First Location allows for any direction of movement.
            areasFound++;
        } else {
            if (state == STATE.WAITING && stateRelation.equalsIgnoreCase("move")){
                lastResponse = response;
                if (response.equalsIgnoreCase("up") || response.equalsIgnoreCase("down") || response.equalsIgnoreCase("left") || response.equalsIgnoreCase("right") ||
                        response.equalsIgnoreCase("north") || response.equalsIgnoreCase("south") || response.equalsIgnoreCase("east") || response.equalsIgnoreCase("west")){
                    DIRECTION dir = DIRECTION.getFromString(response.toLowerCase());
                    if (dir != null && currentArea.canMoveInDir(dir)){
                        if (currentArea.getAreaFromDir(dir) == null){
                            currentArea.setAreaInDir(dir, new Area(dir, currentArea));
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
                            logAction("An event occurred!");
                            runEvent(n, currentArea, currentArea.getType(), "walking `" + response.toUpperCase(Locale.ENGLISH) + "` to");
                        }
                    }else{
	                    sendInformativeMessage(n, "You can't move in that direction! There's something blocking your path!");
                    }
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


    private Area newArea(DIRECTION direction, Area currentLocation) {
        Area a = new Area(direction, currentLocation);
        areas.add(a);
        return a;
    }

    private Area newArea(DIRECTION direction) {
        Area a = new Area(direction);
        areas.add(a);
        return a;
    }
}
