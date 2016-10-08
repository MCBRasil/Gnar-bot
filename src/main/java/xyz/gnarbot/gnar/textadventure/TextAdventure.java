package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.entities.User;
import xyz.gnarbot.gnar.utils.Note;

import java.util.*;

public class TextAdventure {

    private static HashMap<User, TextAdventure> adventures = new HashMap<User, TextAdventure>();

    public static TextAdventure getAdventure(User u, Note n){
        return adventures.containsKey(u) ? adventures.get(u) : new TextAdventure(u, n);
    }

    private Random random;

    protected enum STATE{
        WORKING, WAITING, INTERACTING;
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

    public TextAdventure(User u, Note note){
        this.user = u;
        this.starttime = System.currentTimeMillis();
        this.gameID = UUID.randomUUID();
        this.random = new Random();
        this.random.setSeed(this.starttime);
        System.out.println("Started new Text Adventure for " + u.getUsername() + " (ID: " + gameID.toString() + ")");
        Area startingArea = newArea(DIRECTION.FIRSTMOVE);
        String[] movementOptions = new String[]{"walked towards", "ran towards", "swam to", "were teleported to"};
        note.reply("***A new adventure begins... This is the story of...***\n" +
                "\n" +
                "\n                        :warning:      **Response Required!**      :warning:" +
                "\n  :bulb: :bulb: :bulb:      **What is your name, hero?**      :bulb: :bulb: :bulb:   " +
                "\n ➜ *To answer dialog options, use the _adventure command!*" +
                "\n ➜ *Example: `_adventure " + u.getUsername() + " the Great`*");
    }

    private Area newArea(DIRECTION direction){
        Area a = new Area(direction);
        areas.add(a);
        return a;
    }

}
