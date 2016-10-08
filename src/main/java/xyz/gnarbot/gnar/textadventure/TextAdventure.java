package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import xyz.gnarbot.gnar.utils.Note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
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
        NORTH, SOUTH, EAST, WEST, FIRSTMOVE;
    }

    protected enum ENEMY{
        BOAR, PIG, CHICKEN, MONSTER, GHOST, GHOUL;
    }

    protected class Area{
        private LOCATION locationType;
        private int id;
        private boolean newLocation = true;

        private DIRECTION prevDirect;

        public Area(LOCATION location, DIRECTION prevDirection){
            this.locationType = location;
            this.prevDirect = prevDirection;
            initate();
        }

        public Area(DIRECTION prevDirection){
            this.locationType = LOCATION.values()[random.nextInt(LOCATION.values().length)];
            this.prevDirect = prevDirection;
            initate();
        }

        private void initate(){

        }

        public LOCATION getType(){
            return this.locationType;
        }

    }

    private User user;
    private UUID gameID;
    private Long starttime;
    private ArrayList<Area> areas = new ArrayList<>();
    private String heroName;
    private STATE state;
    private String stateRelation;

    private String lastMessage;
    private Message lastSentMessage;

    public TextAdventure(User u, Note note){
        adventures.put(u, this);
        this.user = u;
        this.starttime = System.currentTimeMillis();
        this.gameID = UUID.randomUUID();
        this.random = new Random();
        this.random.setSeed(this.starttime);
        state = STATE.WAITING_FOR_NAME;
        stateRelation = "waitname";
        System.out.println("Started new Text Adventure for " + u.getUsername() + " (ID: " + gameID.toString() + ")");
        sendMessage(note, "***A new adventure begins... This is the story of... `_________`***\n" +
                "\n" +
                "\n                        :warning:      **Response Required!**      :warning:" +
                "\n  :bulb: :bulb: :bulb:      **What is your name, hero?**      :bulb: :bulb: :bulb:   " +
                "\n ➜ *To answer dialog options, use the `_adventure` command!*" +
                "\n ➜ *Example: `_adventure " + u.getUsername() + " the Great`*");
    }

    private void sendMessage(Note n, String message){
        this.lastMessage = message;
        lastSentMessage = n.reply("\n" + message);
    }

    public void sendLastMessage(Note n, String extra){
        n.reply(extra + "\n" + lastMessage);
    }

    public void parseResponse(Note n, String response){
        System.out.println("Got response for " + user.getUsername() + "'s adventure: \n" + response);
        if (stateRelation.equalsIgnoreCase("waitname") && this.state == STATE.WAITING_FOR_NAME) {
            this.heroName = response.replaceAll("`", "").replaceAll("_", "").replaceAll("\\*", "");
            lastSentMessage.updateMessage("***A new adventure begins... This is the story of... `" + heroName + "`***");
            sendMessage(n, "*A new adventure begins! This is the story of...* ***`" + heroName + "`!***");
            String[] actions = new String[]{"walking into", "running towards", "swimming towards", "teleported to", "suddenly in"};
            Area startArea = newArea(DIRECTION.FIRSTMOVE);
            sendMessage(n, String.format("*We find our hero, %s %s a %s*\n** Available Directions: **\n       :arrow_up:\n" +
                    ":arrow_left:      :arrow_right:\n" +
                    "       :arrow_down:" +
                    "\n :bulb: `Use the _adventure command to go a certain direction! Example: _adventure North`", this.heroName, actions[random.nextInt(actions.length)], startArea.getType().getName()));
        }
    }

    private Area newArea(DIRECTION direction){
        Area a = new Area(direction);
        areas.add(a);
        return a;
    }

}
