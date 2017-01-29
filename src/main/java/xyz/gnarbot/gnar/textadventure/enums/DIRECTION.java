package xyz.gnarbot.gnar.textadventure.enums;

public enum DIRECTION {
    NORTH("north", "up"), SOUTH("south", "down"), EAST("east", "right"), WEST("west", "left"), FIRSTMOVE("firstmove",
            "firstmove");

    private String name;

    private String alt;

    DIRECTION(String name, String alt) {
        this.name = name;
        this.alt = alt;
    }

    public static DIRECTION getFromString(String dir) {
        for (DIRECTION d : values()) {
            if (d.getName().equalsIgnoreCase(dir) || d.getAlt().equalsIgnoreCase(dir)) {
                return d;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getAlt() {
        return alt;
    }
}