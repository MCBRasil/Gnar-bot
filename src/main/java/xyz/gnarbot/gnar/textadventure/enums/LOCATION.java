package xyz.gnarbot.gnar.textadventure.enums;

/**
 * Created by zacha on 11/17/2016.
 */
public enum LOCATION {
	LAKE("Lake", "\uD83C\uDF0A"), RIVER("River", "\uD83C\uDF0A"), FOREST("Forest", "\uD83C\uDF32"), CLEARING("Clearing", "\uD83C\uDF04"), PLAINS("Plains", "\uD83C\uDF04"),
	MOUNTAIN("Mountain", "⛰"), HILL("Hill", "⛰"), HOUSE("House", "\uD83C\uDFE0"), EVIL_HOUSE("House?", "\uD83C\uDFDA"), BEACH("Beach", "⛱"),
	DESERT("Desert", "\uD83C\uDFDC"), DEAD_END("Dead End", "❌");
	private String id;
	private String emote;

	public String getName() {
		return this.id;
	}

	public String getEmote() {
		return emote;
	}

	LOCATION(String id, String emote) {
		this.id = id;
		this.emote = emote;
	}
}