package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.Bot;
import xyz.gnarbot.gnar.textadventure.enums.DIRECTION;
import xyz.gnarbot.gnar.textadventure.enums.LOCATION;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AdventureGrid {

    private final Area[][] xygrid = new Area[15][15];

    private final Adventure relatedAdventure;
    private final Bot bot;

    private int maxSize = 15; // Indicates the maximum and minimum x and y values (convert to negative for minimum)

    private int currentX = 7, currentY = 7;

    public AdventureGrid(Adventure relatedAdventure, Bot bot) {
        this.relatedAdventure = relatedAdventure;
        this.bot = bot;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Adventure getRelatedAdventure() {
        return relatedAdventure;
    }

    public Area getAreaAtLocation(int x, int y) {
        return xygrid[x][y];
    }

    public int getCurrentX() {
        return currentX;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public Area getCurrentArea() {
        return getAreaAtLocation(getCurrentX(), getCurrentY());
    }

    public Area getAreaInDirection(DIRECTION dir) {
        if (dir == DIRECTION.NORTH) {
            return getAreaAtLocation(getCurrentX(), getCurrentY() - 1);
        }
        if (dir == DIRECTION.SOUTH) {
            return getAreaAtLocation(getCurrentX(), getCurrentY() + 1);
        }
        if (dir == DIRECTION.EAST) {
            return getAreaAtLocation(getCurrentX() + 1, getCurrentY());
        }
        if (dir == DIRECTION.WEST) {
            return getAreaAtLocation(getCurrentX() - 1, getCurrentY());
        }
        return null;
    }

    public boolean moveInDirection(DIRECTION dir) {
        if (dir == DIRECTION.NORTH) {
            if (getAreaInDirection(dir).getType() != LOCATION.DEAD_END) {
                setCurrentY(getCurrentY() - 1);
            } else {
                getAreaInDirection(dir).discover();
                return false;
            }
            return true;
        }
        if (dir == DIRECTION.SOUTH) {
            if (getAreaInDirection(dir).getType() != LOCATION.DEAD_END) {
                setCurrentY(getCurrentY() + 1);
            } else {
                getAreaInDirection(dir).discover();
                return false;
            }
            return true;
        }
        if (dir == DIRECTION.EAST) {
            if (getAreaInDirection(dir).getType() != LOCATION.DEAD_END) {
                setCurrentX(getCurrentX() + 1);
            } else {
                getAreaInDirection(dir).discover();
                return false;
            }
            return true;
        }
        if (dir == DIRECTION.WEST) {
            if (getAreaInDirection(dir).getType() != LOCATION.DEAD_END) {
                setCurrentX(getCurrentX() - 1);
            } else {
                getAreaInDirection(dir).discover();
                return false;
            }
            return true;
        }
        return false;
    }

    public void beginBuild() {
        int curX, curY;
        for (curX = 0; curX < getMaxSize(); curX++) {
            for (curY = 0; curY < getMaxSize(); curY++) {
                if (curX == 0 || curX == getMaxSize() - 1 || curY == 0 || curY == getMaxSize() - 1) {
                    Area a = new Area(relatedAdventure, LOCATION.DEAD_END);
                    a.discover();
                    xygrid[curX][curY] = a;
                } else {
                    Area a;
                    if (curX == currentX && curY == currentY) {
                        a = new Area(relatedAdventure, LOCATION.CLEARING);
                    } else {
                        a = new Area(relatedAdventure);
                    }
                    if (getRelatedAdventure().getHeroName().toLowerCase().contains("cheat")) {
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
    }

    public void sendMap(Message msg) {
        try {
            ImageBuilder builder = new ImageBuilder();
            File mapFile = builder.runBuilder();
            if (mapFile == null) {
                msg.respond().error("Couldn't create map file. Notify @Gatt#9711 please.").queue();
                return;
            }

            bot.getScheduler().schedule(() ->
                    msg.respond().info("Sending your map!").queue(it -> {
                        try {
                            msg.getChannel().sendFile(mapFile, it).queue();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }), 1, TimeUnit.SECONDS);

            bot.getScheduler().schedule(() ->
            {
                if (!mapFile.delete()) {
                    bot.getLog().warn("Unable to delete map file.");
                }
                mapFile.deleteOnExit();
            }, 10, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ImageBuilder extends JComponent {
        private Image img;

        private Integer x, y, resizeX, resizeY;

        public ImageBuilder setImg(Image img) {
            this.img = img;
            return this;
        }

        public void setResizeX(int resizeX) {
            this.resizeX = resizeX;
        }

        public void setResizeY(int resizeY) {
            this.resizeY = resizeY;
        }

        public ImageBuilder setX(int x) {
            this.x = x;
            return this;
        }

        public ImageBuilder setY(int y) {
            this.y = y;
            return this;
        }

        public File runBuilder() {
            final File mapFile = new File("data/temp/adventures/maps/_map.png");
            if (!mapFile.exists()) {
                if(!mapFile.mkdirs()) {
                    return null;
                }
            }
            try {
                BufferedImage map = new BufferedImage(getMaxSize() * 64, (getMaxSize() * 64) + 200, BufferedImage
                        .TYPE_INT_ARGB);
                Graphics2D graphics = map.createGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, getMaxSize() * 64, getMaxSize() * 64 + 200);
                graphics.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 40));
                graphics.setColor(Color.BLACK);
                int curX, curY;
                int printX = 0, printY = 200;
                for (curY = 0; curY < getMaxSize(); curY++) {
                    for (curX = 0; curX < getMaxSize(); curX++) {
                        Area a = getAreaAtLocation(curX, curY);
                        if (curY == currentY && curX == currentX) {
                            Image img = ImageIO.read(new File
                                    ("data/resources/adventure/icons/64/position-marker.png"));
                            setImg(img).setX(printX).setY(printY).paintComponent(graphics);
                        } else {
                            if (a != null) {
                                if (a.isDiscovered()) {
                                    Image img = ImageIO.read(new File("data/resources/adventure/icons/64/" +
                                            a.getType()
                                                    .getFile() + ".png"));
                                    setImg(img).setX(printX).setY(printY).paintComponent(graphics);
                                } else {
                                    Image img = ImageIO.read(new File
                                            ("data/resources/adventure/icons/64/unknown.png"));
                                    setImg(img).setX(printX).setY(printY).paintComponent(graphics);
                                }
                            } else {
                                Image img = ImageIO.read(new File("data/resources/adventure/icons/64/unknown" +
                                        ".png"));
                                setImg(img).setX(printX).setY(printY).paintComponent(graphics);
                            }
                        }
                        printX += 64;
                    }
                    printX = 0;
                    printY += 64;
                }
                Image img = ImageIO.read(new File("data/resources/adventure/icons/64/position-marker.png"));
                setImg(img).setX(8).setY(8).paintComponent(graphics);
                graphics.drawString(" This is you! Current area: " + getAreaAtLocation(getCurrentX(), getCurrentY())
                        .getType()
                        .getName(), 16, 32);
                Image img2 = ImageIO.read(new File("data/resources/adventure/icons/64/" + getAreaAtLocation
                        (getCurrentX(), getCurrentY())
                        .getType()
                        .getFile() + ".png"));
                setImg(img2).setX(map.getWidth() - 32).setY(16).setResizeX(32);
                setResizeY(32);
                paintComponent(graphics);
                drawCenteredString(graphics, getRelatedAdventure().getHeroName() + "'s Map", new Rectangle(getMaxSize
                        () * 64, 200), graphics
                        .getFont());
                ImageIO.write(map, "png", mapFile);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return mapFile;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (resizeX == null) {
                g.drawImage(img, x, y, this); // draw background image
            } else {
                g.drawImage(img, x, y, resizeX, resizeY, this);

            }
        }
    }
}
