package xyz.gnarbot.gnar.textadventure;

import xyz.gnarbot.gnar.textadventure.enums.DIRECTION;
import xyz.gnarbot.gnar.textadventure.enums.LOCATION;

/**
 * Created by zacha on 11/17/2016.
 */
public class AdventureGrid {

	private Area[][] xygrid = new Area[15][15];
	private TextAdventure relatedAdventure;
	private int maxSize = 15; // Indicates the maximum and minimum x and y values (convert to negative for minimum)

	private int currentX = 7, currentY = 7;

	public AdventureGrid(TextAdventure relatedAdventure) {
		this.relatedAdventure = relatedAdventure;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public TextAdventure getRelatedAdventure() {
		return relatedAdventure;
	}

	public Area getAreaAtLocation(Integer x, Integer y){
		return (xygrid[x][y] != null) ? xygrid[x][y] : null;
	}

	public int getCurrentX() {
		return currentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

	public boolean moveInDirection(DIRECTION dir){
		if (dir == DIRECTION.NORTH){
			if (getAreaAtLocation(getCurrentX(), getCurrentY() + 1).getType() != LOCATION.DEAD_END) {
				setCurrentY(getCurrentY() + 1);
			}else{
				return false;
			}
			return true;
		}
		if (dir == DIRECTION.SOUTH){
			if (getAreaAtLocation(getCurrentX(), getCurrentY() - 1).getType() != LOCATION.DEAD_END) {
				setCurrentY(getCurrentY() - 1);
			}else{
				return false;
			}
			return true;
		}
		if (dir == DIRECTION.EAST){
			if (getAreaAtLocation(getCurrentX() + 1, getCurrentY()).getType() != LOCATION.DEAD_END) {
				setCurrentX(getCurrentX() + 1);
			}else{
				return false;
			}
			return true;
		}
		if (dir == DIRECTION.WEST){
			if (getAreaAtLocation(getCurrentX() - 1, getCurrentY()).getType() != LOCATION.DEAD_END) {
				setCurrentX(getCurrentX() - 1);
			}else{
				return false;
			}
			return true;
		}
		return false;
	}

	public void beginBuild(){
		int curX, curY;
		for (curX = 0; curX < getMaxSize(); curX++){
			for (curY = 0; curY < getMaxSize(); curY++) {
				if (curX == 0 || curX == getMaxSize()-1 || curY == 0 || curY == getMaxSize()-1){
					Area a = new Area(relatedAdventure, LOCATION.DEAD_END);
					a.discover();
					xygrid[curX][curY] = a;
				}else{
					Area a = new Area(relatedAdventure);
					if (getRelatedAdventure().getRandom().nextInt() * 100 < 6){
						a.discover();
					}
					xygrid[curX][curY] = a;
				}

			}
		}
	}

	public String buildMap(){
		String rtrn = "";
		int curX, curY;
		for (curY = 0; curY < getMaxSize(); curY++) {
			for (curX = 0; curX < getMaxSize(); curX++){
				if (curY == currentY && curX == currentX){
					rtrn += "\uD83D\uDEB6";
				}else{
					Area a = getAreaAtLocation(curX, curY);
					if (a != null) {
						if (a.isDiscovered()) {
							rtrn += a.getType().getEmote();
						}else{
							rtrn += "❓";
						}
					}else{
						rtrn += "❓";
					}
				}
				rtrn+="   ";

			}
			rtrn += "\n\n";
		}
		return rtrn;
	}
}
