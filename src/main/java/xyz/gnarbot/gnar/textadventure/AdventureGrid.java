package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.textadventure.enums.DIRECTION;
import xyz.gnarbot.gnar.textadventure.enums.LOCATION;
import xyz.gnarbot.gnar.utils.Note;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

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

	public Area getAreaInDirection(DIRECTION dir){
		if (dir == DIRECTION.NORTH){
			return getAreaAtLocation(getCurrentX(), getCurrentY() + 1);
		}
		if (dir == DIRECTION.SOUTH){
			return getAreaAtLocation(getCurrentX(), getCurrentY() - 1);
		}
		if (dir == DIRECTION.EAST) {
			return getAreaAtLocation(getCurrentX() + 1, getCurrentY());
		}
		if (dir == DIRECTION.WEST) {
			return getAreaAtLocation(getCurrentX() - 1, getCurrentY());
		}
		return null;
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

	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
		// Get the FontMetrics
		FontMetrics metrics = g.getFontMetrics(font);
		int x = (rect.width - metrics.stringWidth(text)) / 2;
		int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		g.setFont(font);
		g.drawString(text, x, y);
		g.dispose();
	}

	public void sendMap(Note n){
		/*String rtrn = "";

		return rtrn;*/
		try {
			final File mapFile = new File("_temp/adventures/maps/" + getRelatedAdventure().getGameID().toString() + "/map.png");
			if (!mapFile.exists()) {
				mapFile.mkdirs();
			}
			/*
			BufferedImage map = new BufferedImage(getMaxSize() * 64, (getMaxSize() * 64) + 200, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = map.createGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, getMaxSize()* 64, getMaxSize()*64 + 200);
			graphics.setFont(new Font(Font.DIALOG_INPUT, 25, 40));
			graphics.setColor(Color.BLACK);
			drawCenteredString(graphics, getRelatedAdventure().getHeroName() + "'s Map", new Rectangle(getMaxSize() * 64, 200), graphics.getFont());

			graphics.
			graphics.setColor(Color.BLACK);

			graphics.drawString("Why tho", 128, 128);

			Canvas canvas = new Canvas();
			canvas.snapshot(null, new WritableImage(getMaxSize() * 64, (getMaxSize() * 64) + 200));
			canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getHeight(), canvas.getWidth());
			canvas.getGraphicsContext2D().setFontSmoothingType(FontSmoothingType.GRAY);*/

			BufferedImage map = new BufferedImage(getMaxSize() * 64, (getMaxSize() * 64) + 200, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = map.createGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, getMaxSize()* 64, getMaxSize()*64 + 200);
			graphics.setFont(new Font(Font.DIALOG_INPUT, 25, 40));
			graphics.setColor(Color.BLACK);
			drawCenteredString(graphics, getRelatedAdventure().getHeroName() + "'s Map", new Rectangle(getMaxSize() * 64, 200), graphics.getFont());

			graphics.setFont(new Font(Font.DIALOG_INPUT, 25, 40));
			graphics.setColor(Color.BLACK);

			graphics.drawString("Why tho", 128, 128);

			int curX, curY;
			int printX = 0, printY = 200;
			for (curY = 0; curY < getMaxSize(); curY++) {
				for (curX = 0; curX < getMaxSize(); curX++){
					Area a = getAreaAtLocation(curX, curY);
					if (curY == currentY && curX == currentX){
						Image img = ImageIO.read(new File("_data/adventureresources/locationicons/64/position-marker.png"));
						graphics.drawImage(img, printX, printY, 64, 64, null);
						System.out.println("drew a position marker at " + printX + ", " + printY);
						graphics.drawString("u", printX, printY);
					}else{
						if (a != null) {
							if (a.isDiscovered()) {
								Image img = ImageIO.read(new File("_data/adventureresources/locationicons/64/" + a.getType().getFile() + ".png"));
								System.out.println("drew a " + a.getType().getFile() + " at " + printX + ", " + printY);
								graphics.drawImage(img, printX, printY, 64, 64, null);
								graphics.drawString("d", printX, printY);
							}else{
								Image img = ImageIO.read(new File("_data/adventureresources/locationicons/64/unknown.png"));
								graphics.drawImage(img, printX, printY, 64, 64, null);
								System.out.println("drew an unknown icon at " + printX + ", " + printY);
								graphics.drawString("?", printX, printY);
							}
						}else{
							Image img = ImageIO.read(new File("_data/adventureresources/locationicons/64/unknown.png"));
							graphics.drawImage(img, printX, printY, 64, 64, null);
							System.out.println("drew an unknown icon at " + printX + ", " + printY);
							graphics.drawString("?", printX, printY);
						}
					}
					printX+=64;
				}
				printX = 0;
				printY+=64;
			}
			Bot.INSTANCE.getScheduler().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						ImageIO.write(map, "png", mapFile);
						System.out.println("Wrote to file");
					}catch (IOException e){
						e.printStackTrace();
					}
				}
			}, 3, TimeUnit.SECONDS);

			Bot.INSTANCE.getScheduler().schedule(new Runnable() {
				@Override
				public void run() {
					try {
						Message m = n.reply("Sending...");
						n.getChannel().sendFile(mapFile, m).queue();
						m.editMessage("Here's your map!").queue();
					}catch (IOException e){
						e.printStackTrace();
					}
				}
			}, 4, TimeUnit.SECONDS);

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
