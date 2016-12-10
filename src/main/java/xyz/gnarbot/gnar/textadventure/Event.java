package xyz.gnarbot.gnar.textadventure;

import xyz.gnarbot.gnar.utils.Note;

/**
 * Created by zacha on 11/1/2016.
 */
public class Event {

	private String eventID, eventName, eventDescription;
	private boolean completedEvent = false;

	public Event(){
	}

	public Event(String eventID, String eventName, String eventDescription){
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

	public void runEvent(Adventure adventure, Note n){

	}

	public void parseResponse(Adventure adventure, Note n, String response){

	}

	public void sendMessage(Note n, String message) {
		n.reply("\n" + message);
	}

	public void setCompletedEvent(boolean completedEvent) {
		this.completedEvent = completedEvent;
	}

	public String toString(){
		return "Event: " + getEventName() + " EventID: " + getEventID() + " Description: " + getEventDescription();
	}
}
