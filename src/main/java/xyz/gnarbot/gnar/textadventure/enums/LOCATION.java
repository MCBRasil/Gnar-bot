package xyz.gnarbot.gnar.textadventure.enums;

/**
 * Created by zacha on 11/17/2016.
 */
public enum LOCATION {
	LAKE("Lake", "lily-pads"), RIVER("River", "river"), FOREST("Forest", "forest"), CLEARING("Clearing", "hills"), PLAINS("Plains", "hills"),
	MOUNTAIN("Mountain", "peaks"), HILL("Hill", "hills"), HOUSE("House", "village"), EVIL_HOUSE("House?", "arson"), BEACH("Beach", "beach-ball"),
	DESERT("Desert", "egyptian-sphinx"), CALDERA("Volcano", "caldera"), CHURCH("Church", "church"), VOLCANO("Volcano", "volcano"),
	GRAVEYARD("Graveyard", "graveyard"), HILL_FORT("Fort", "hill-fort"), TOWER("Stone Tower", "stone-tower"), DESTROYED_TOWER("Fallen Tower", "tower-fall"),
	LIGHTHOUSE("Lighthouse", "lighthouse"), DEAD_END("Dead End", "brick-wall");
	private String id;
	private String file;

	public String getName() {
		return this.id;
	}

	public String getFile() {
		return file;
	}

	LOCATION(String id, String file) {
		this.id = id;
		this.file = file;
	}
}