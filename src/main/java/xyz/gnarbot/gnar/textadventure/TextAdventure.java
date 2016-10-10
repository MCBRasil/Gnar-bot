package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import xyz.gnarbot.gnar.utils.Note;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TextAdventure {

    private static HashMap<User, TextAdventure> adventures = new HashMap<User, TextAdventure>();

    public static TextAdventure getAdventure(User u, Note n){
        System.out.println(u.getUsername() + " requested a Text Adventure");
        if (adventures.containsKey(u)){
            return adventures.get(u);
        }else{
            return new TextAdventure(u, n);
        }
    }

    private Random random;

    protected enum STATE{
        WORKING, WAITING, INTERACTING, RESPONSE_REQUIRED, WAITING_FOR_NAME;
    }

    protected enum LOCATION{
        LAKE("Lake"), RIVER("River"), FOREST("Forest"), CLEARING("Clearing"), PLAINS("Plains"),
        MOUNTAIN("Mountain"), HILL("Hill"), HOUSE("House"), EVIL_HOUSE("House"), BEACH("Beach"),
        DESERT("Desert"), DEAD_END("Dead End");
        private String id;

        public String getName(){
            return this.id;
        }

        LOCATION(String id){
            this.id = id;
        }
    }

    protected enum DIRECTION{
        NORTH("north"), SOUTH("south"), EAST("east"), WEST("west"), FIRSTMOVE("firstmove");
        private String name;

        public String getName() {
            return name;
        }

        public static DIRECTION getFromString(String dir){
            for (DIRECTION d : values()){
                if (d.getName().equalsIgnoreCase(dir)){
                    return d;
                }
            }
            return null;
        }

        DIRECTION(String name){
            this.name = name;
        }
    }

    protected enum ENEMY{
        BOAR, PIG, CHICKEN, MONSTER, GHOST, GHOUL;
    }

    protected class Area{
        private LOCATION locationType;
        private int id;
        private boolean newLocation = true;

        private Area areaNorth, areaSouth, areaEast, areaWest, connectedArea;

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

        public Area(LOCATION location, DIRECTION prevDirection){
            this.locationType = location;
            this.prevDirect = prevDirection;
            initate();
        }

        public Area(DIRECTION prevDirection, Area previousArea){
            this.locationType = LOCATION.values()[random.nextInt(LOCATION.values().length)];
            this.prevDirect = prevDirection;
            this.connectedArea = previousArea;
            if (prevDirection == DIRECTION.NORTH){
                setAreaSouth(previousArea);
            }
            if (prevDirection == DIRECTION.SOUTH){
                setAreaNorth(previousArea);
            }
            if (prevDirection == DIRECTION.EAST){
                setAreaWest(previousArea);
            }
            if (prevDirection == DIRECTION.WEST){
                setAreaEast(previousArea);
            }

            initate();
        }

        public Area(LOCATION locationType, DIRECTION prevDirection, Area previousArea){
            this.locationType = locationType;
            this.prevDirect = prevDirection;
            this.connectedArea = previousArea;
            if (prevDirection == DIRECTION.NORTH){
                setAreaSouth(previousArea);
            }
            if (prevDirection == DIRECTION.SOUTH){
                setAreaNorth(previousArea);
            }
            if (prevDirection == DIRECTION.EAST){
                setAreaWest(previousArea);
            }
            if (prevDirection == DIRECTION.WEST){
                setAreaEast(previousArea);
            }

            initate();
        }

        public Area(DIRECTION prevDirection){
            this.locationType = LOCATION.values()[random.nextInt(LOCATION.values().length)];
            this.prevDirect = prevDirection;
            initate();
        }

        private void initate(){
            if (prevDirect == DIRECTION.FIRSTMOVE){
                setAreaNorth(new Area(DIRECTION.NORTH, this));
                setAreaEast(new Area(DIRECTION.EAST, this));
                setAreaSouth(new Area(DIRECTION.SOUTH, this));
                setAreaWest(new Area(DIRECTION.WEST, this));
            }
        }

        public boolean canMoveNorth(){
            return getAreaNorth() != null;
        }

        public boolean canMoveEast(){
            return getAreaEast() != null;
        }

        public boolean canMoveWest(){
            return getAreaWest() != null;
        }

        public boolean canMoveSouth(){
            return getAreaSouth() != null;
        }

        public LOCATION getType(){
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

    private Area currentArea, startArea;

    public TextAdventure(User u, Note note){
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

    public void logAction(String action){
        actionList.add("[" +  new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + "] " + action);
        System.out.println("[" +  new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + "] " + action);
    }

    public void sendMessage(Note n, String message){
        this.lastMessage = message;
        lastSentMessage = n.reply("\n" + message);
    }

    public void sendInformativeMessage(Note n, String message){
        lastSentMessage = n.reply("\n" + message);
    }

    public void sendLastMessage(Note n, String extra){
        n.reply(extra + "\n" + lastMessage);
    }

    private String getNewLocationText(Area originArea, LOCATION locationType, String action){

        String r = String.format("*We find our hero, %s %s a %s*\n** Available Directions: **\n       <north>\n" +
                "<west>      <east>\n" +
                "       <south>" +
                "\n :bulb: `Use the _adventure command to go a certain direction! Example: _adventure North`", this.heroName, action, locationType.getName());
        if (originArea.canMoveNorth()){
            r = r.replaceAll("<north>", ":arrow_up:");
        }else{
            r = r.replaceAll("<north>", ":negative_squared_cross_mark:");
        }
        if (originArea.canMoveSouth()){
            r = r.replaceAll("<south>", ":arrow_down:");
        }else{
            r = r.replaceAll("<south>", ":negative_squared_cross_mark:");
        }
        if (originArea.canMoveEast()){
            r = r.replaceAll("<east>", ":arrow_right:");
        }else{
            r = r.replaceAll("<east>", ":negative_squared_cross_mark:");
        }
        if (originArea.canMoveWest()){
            r = r.replaceAll("<west>", ":arrow_left:");
        }else{
            r = r.replaceAll("<west>", ":negative_squared_cross_mark:");
        }

        return r;
    }

    public void parseResponse(Note n, String response){
        System.out.println("Got response for " + user.getUsername() + "'s adventure: \n" + response);
        if (stateRelation.equalsIgnoreCase("waitname") && this.state == STATE.WAITING_FOR_NAME) {
            setHeroName(response);
            lastSentMessage.updateMessage("***A new adventure begins... This is the story of... `" + heroName + "`***");
            sendMessage(n, "*A new adventure begins! This is the story of...* ***`" + heroName + "`!***");
            String[] actions = new String[]{"walking into", "running towards", "swimming towards", "teleported to", "suddenly in"};
            state = STATE.WAITING;
            stateRelation = "null";
            startArea = newArea(DIRECTION.FIRSTMOVE);
            currentArea = startArea;
            sendMessage(n, getNewLocationText(currentArea, startArea.getType(), actions[random.nextInt(actions.length)])); // First Location allows for any direction of movement.
        }else{
            sendInformativeMessage(n, "I'm unsure of what you meant by `"+ response+"`. Type `_adventure help` to bring up the Help Menu."); // Placeholder until I add the moving system.
        }
    }

    public void setHeroName(String heroName){
        this.heroName = heroName.replaceAll("`", "").replaceAll("_", "").replaceAll("\\*", "");
    }

    public String getHeroName() {
        return heroName;
    }

    public ArrayList<String> getActionList() {
        return actionList;
    }

    private Area newArea(DIRECTION direction, Area currentLocation){
        Area a = new Area(direction, currentLocation);
        areas.add(a);
        return a;
    }

    private Area newArea(DIRECTION direction){
        Area a = new Area(direction);
        areas.add(a);
        return a;
    }

}
