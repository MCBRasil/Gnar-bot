package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import xyz.gnarbot.gnar.utils.Note;

import java.awt.*;

public class Event {
    private String eventID, eventName, eventDescription;

    private boolean completedEvent = false;

    public Event() {
    }

    public Event(String eventID, String eventName, String eventDescription) {
        this.eventDescription = eventDescription;
        this.eventName = eventName;
        this.eventID = eventID;
        System.out.println(this.toString());
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public boolean hasCompletedEvent() {
        return completedEvent;
    }

    public Event runEvent(Adventure adventure, Note n) {
        return this;
    }

    public void parseResponse(Adventure adventure, Note n, String response) {

    }

    public void sendMessage(Note n, String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**" + n.getAuthor().getName() + "'s Adventure**")
                .addBlankField(true)
                .setDescription(message)
                .setColor(new Color(39, 255, 9));
        MessageEmbed embed = eb.build();
        MessageBuilder mb = new MessageBuilder();
        mb.setEmbed(embed);
        Message m = mb.build();
        n.getChannel().sendMessage(m).queue();
    }

    public void sendMessage(Note n, String message, String imageurl) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("**" + n.getAuthor().getName() + "'s Adventure**")
                .addBlankField(true)
                .setDescription(message)
                .setColor(new Color(39, 255, 9));
        eb.setThumbnail(imageurl);
        MessageEmbed embed = eb.build();
        MessageBuilder mb = new MessageBuilder();
        mb.setEmbed(embed);
        Message m = mb.build();
        n.getChannel().sendMessage(m).queue();
    }

    public void setCompletedEvent(boolean completedEvent) {
        this.completedEvent = completedEvent;
    }

    public String toString() {
        return "Event: " + getEventName() + " EventID: " + getEventID() + " Description: " + getEventDescription();
    }
}
