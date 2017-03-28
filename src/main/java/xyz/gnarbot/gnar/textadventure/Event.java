package xyz.gnarbot.gnar.textadventure;

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

    public void sendMessage(Note note, String message) {
        sendMessage(note, message, null);
    }

    public void sendMessage(Note note, String message, String image) {
        note.respond().embed(note.getAuthor().getName() + "'s Adventure")
                .field(true)
                .setDescription(message)
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
