package xyz.gnarbot.gnar.textadventure;

import xyz.gnarbot.gnar.textadventure.enums.DIRECTION;
import xyz.gnarbot.gnar.textadventure.enums.LOCATION;
import xyz.gnarbot.gnar.textadventure.events.FirstBagEvent;
import xyz.gnarbot.gnar.textadventure.events.FirstSwordEvent;
import xyz.gnarbot.gnar.textadventure.events.FirstSwordLewdEvent;

import java.util.Random;

public class Area {
    private LOCATION locationType;

    private int id;

    private boolean newLocation = true;

    private Event relatedEvent;

    private Random random;

    private Adventure relatedAdventure;

    private boolean discovered = false;

    private DIRECTION prevDirect;

    public Area(Adventure adventure, LOCATION location) {
        this.relatedAdventure = adventure;
        this.locationType = location;
        this.random = adventure.getRandom();
        initate();
    }

    public Area(Adventure adventure) {
        this.random = adventure.getRandom();
        this.relatedAdventure = adventure;
        this.locationType = LOCATION.values()[random.nextInt(LOCATION.values().length)];

        initate();
    }

    public Area(Adventure adventure, DIRECTION prevDirection) {
        this.random = adventure.getRandom();
        this.relatedAdventure = adventure;
        this.locationType = LOCATION.values()[random.nextInt(LOCATION.values().length)];
        this.prevDirect = prevDirection;

        initate();
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void discover() {
        this.discovered = true;
    }

    public Event getRelatedEvent() {
        return relatedEvent;
    }

    public boolean isNewLocation() {
        return newLocation;
    }

    public Adventure getRelatedAdventure() {
        return relatedAdventure;
    }

    public boolean moveToHere() {
        if (this.newLocation) {
            this.newLocation = false;
            System.out.println("New location. ID: " + getRelatedAdventure().getAreasFound());
            if (getRelatedAdventure().getAreasFound() == 4) {
                relatedEvent = new FirstBagEvent();
                System.out.println("Created an Event!");
            }
            if (getRelatedAdventure().getAreasFound() > 6) {
                if (random.nextInt() * 100 > 80) {
                    if (getRelatedAdventure().getHeroName()
                            .toLowerCase()
                            .contains("lewd") || getRelatedAdventure().getHeroName().toLowerCase().contains("mae")) {
                        relatedEvent = new FirstSwordLewdEvent();
                    } else {
                        relatedEvent = new FirstSwordEvent();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean hasCompletedEvent() {
        return relatedEvent == null || relatedEvent.hasCompletedEvent();
    }

    private void initate() {
        /*
        if (prevDirect == DIRECTION.FIRSTMOVE) {
			setAreaNorth(new Area(relatedAdventure, DIRECTION.NORTH, this));
			setAreaEast(new Area(relatedAdventure, DIRECTION.EAST, this));
			setAreaSouth(new Area(relatedAdventure, DIRECTION.SOUTH, this));
			setAreaWest(new Area(relatedAdventure, DIRECTION.WEST, this));
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
		}*/
    }

    public LOCATION getType() {
        return this.locationType;
    }

}
