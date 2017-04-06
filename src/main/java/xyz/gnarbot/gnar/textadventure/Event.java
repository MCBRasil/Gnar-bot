package xyz.gnarbot.gnar.textadventure;

import net.dv8tion.jda.core.entities.Message;

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

    public Event runEvent(Adventure adventure, Message message) {
        return this;
    }

    public void parseResponse(Adventure adventure, Message message, String response) {

    }

    public void sendMessage(Message message, String text) {
        sendMessage(message, text, null);
    }

    public void sendMessage(Message message, String text, String image) {
        message.respond().embed(message.getAuthor().getName() + "'s Adventure")
                .field(true)
                .setDescription(text)
                .setColor(new Color(39, 255, 9))
                .setThumbnail(image)
                .rest().queue();
    }

    public void setCompletedEvent(boolean completedEvent) {
        this.completedEvent = completedEvent;
    }

    public String toString() {
        return "Event: " + getEventName() + " EventID: " + getEventID() + " Description: " + getEventDescription();
    }
}
