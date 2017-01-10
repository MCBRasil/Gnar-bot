package xyz.gnarbot.gnar.textadventure.enums;

/* http://imgur.com/a/IknLm Album here */
public enum LOCATION
{
    LAKE("Lake", "lily-pads", "http://i.imgur.com/PPv0U1d.png"), RIVER("River", "river", "http://i.imgur.com/CLQJSOE"
        + ".png"), FOREST("Forest", "forest", "http://i.imgur.com/B6UMHjs.png"), CLEARING("Clearing", "hills",
        "http://i.imgur.com/1KhZa1n.png"), PLAINS("Plains", "hills", "http://i.imgur.com/1KhZa1n.png"), MOUNTAIN
        ("Mountain", "peaks", "http://i.imgur.com/h0Px5A6.png"), HILL("Hill", "hills", "http://i.imgur.com/1KhZa1n" +
        ".png"), HOUSE("House", "village", "http://i.imgur.com/QArrgzS.png"), EVIL_HOUSE("House?", "arson",
        "http://i" + ".imgur.com/PHJH199.png"), BEACH("Beach", "beach-ball", "http://i.imgur.com/LLYTieq.png"),
    DESERT("Desert", "egyptian-sphinx", "http://i.imgur.com/8mc463k.png"), CALDERA("Volcano", "caldera", "http://i" +
        ".imgur" + ".com/7S3yI4O.png"), CHURCH("Church", "church", "http://i.imgur.com/RvUmWnV.png"), VOLCANO
        ("Volcano", "volcano", "http://i.imgur.com/y9w5Dz5.png"), GRAVEYARD("Graveyard", "graveyard", "http://i" +
        ".imgur" + ".com/QQEvMu8.png"), HILL_FORT("Fort", "hill-fort", "http://i.imgur.com/NMih4aC.png"), TOWER
        ("Stone Tower", "stone-tower", "http://i.imgur.com/gKVUNVP.png"), DESTROYED_TOWER("Fallen Tower",
        "tower-fall", "http://i" + ".imgur.com/DIcu9HJ.png"), LIGHTHOUSE("Lighthouse", "lighthouse", "http://i.imgur" +
        ".com/8PaBfXQ.png"), DEAD_END("Dead End", "brick-wall", "http://i.imgur.com/xFZvoIN.png");
    
    private String id;
    
    private String file;
    
    private String url;
    
    LOCATION(String id, String file, String url)
    {
        this.id = id;
        this.file = file;
        this.url = url;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public String getName()
    {
        return this.id;
    }
    
    public String getFile()
    {
        return file;
    }
}