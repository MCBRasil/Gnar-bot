package xyz.gnarbot.gnar.textadventure.enums;

/**
 * Created by zacha on 11/17/2016.
 */
public enum LOCATION {
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