package xyz.gnarbot.gnar.textadventure;

import xyz.gnarbot.gnar.textadventure.enums.DIRECTION;
import xyz.gnarbot.gnar.textadventure.enums.LOCATION;
import xyz.gnarbot.gnar.textadventure.events.FirstBagEvent;
import xyz.gnarbot.gnar.textadventure.events.FirstSwordEvent;
import xyz.gnarbot.gnar.textadventure.events.FirstSwordLewdEvent;

import java.util.Random;

/**
 * Created by zacha on 11/17/2016.
 */
public class Area {

	private LOCATION locationType;
	private int id;
	private boolean newLocation = true;

	private Area areaNorth, areaSouth, areaEast, areaWest, connectedArea;
	private boolean canMoveNorth = true, canMoveSouth = true, canMoveEast = true, canMoveWest = true, hasEvent = false;

	private Event relatedEvent;

	private Random random;

	private TextAdventure relatedAdventure;

	public Event getRelatedEvent() {
		return relatedEvent;
	}

	public boolean isNewLocation() {
		return newLocation;
	}

	public TextAdventure getRelatedAdventure() {
		return relatedAdventure;
	}

	public boolean moveToThis(){
		if (this.newLocation) {
			this.newLocation = false;
			System.out.println("New location. ID: " + getRelatedAdventure().getAreasFound());
			if (getRelatedAdventure().getAreasFound() == 4){
				hasEvent = true;
				relatedEvent = new FirstBagEvent();
				System.out.println("Created an Event!");
			}
			if (getRelatedAdventure().getAreasFound() > 6){
				if (random.nextInt()*100 > 80){
					hasEvent = true;
					if (getRelatedAdventure().getHeroName().toLowerCase().contains("lewd") || getRelatedAdventure().getHeroName().toLowerCase().contains("mae")){
						relatedEvent = new FirstSwordLewdEvent();
					}else {
						relatedEvent = new FirstSwordEvent();
					}
				}
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

	public Area(TextAdventure adventure, LOCATION location, DIRECTION prevDirection) {
		this.relatedAdventure = adventure;
		this.locationType = location;
		this.prevDirect = prevDirection;
		this.random = adventure.getRandom();
		initate();
	}

	public Area(TextAdventure adventure, DIRECTION prevDirection, Area previousArea) {
		this.relatedAdventure = adventure;
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

	public Area(TextAdventure adventure, DIRECTION prevDirection) {
		this.relatedAdventure = adventure;
		this.locationType = LOCATION.values()[random.nextInt(LOCATION.values().length)];
		this.prevDirect = prevDirection;

		initate();
	}

	private void initate() {
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
