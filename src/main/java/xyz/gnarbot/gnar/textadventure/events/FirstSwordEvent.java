package xyz.gnarbot.gnar.textadventure.events;

import xyz.gnarbot.gnar.textadventure.Adventure;
import xyz.gnarbot.gnar.textadventure.Event;
import xyz.gnarbot.gnar.textadventure.Item;
import xyz.gnarbot.gnar.utils.Note;

public class FirstSwordEvent extends Event
{
    public FirstSwordEvent()
    {
        super("FirstSword", "First Sword", "Get your first Sword");
    }
    
    @Override
    public String getEventID()
    {
        return super.getEventID();
    }
    
    @Override
    public String getEventName()
    {
        return "First Sword";
    }
    
    @Override
    public String getEventDescription()
    {
        return "You found a Sword on the floor!";
    }
    
    @Override
    public boolean hasCompletedEvent()
    {
        return super.hasCompletedEvent();
    }
    
    @Override
    public Event runEvent(Adventure adventure, Note n)
    {
        super.sendMessage(n, ":bulb: *crash* Something lands in front of you." + "\n As the dust settles you see a " +
                "Sword stuck in the ground with it's hilt pointing upwards," + " and you feel some force bringing you" +
                " closer to it." + "\n   :warning: What would you like to do?\n  " + "➜ `Pick it up`\n  ➜ `Leave " +
                "it`\n  ➜ `Examine it`\n \n " + ":bulb: `Use the _adventure command to select a response! Example: " +
                "_adventure Pick it up`");
        return this;
    }
    
    @Override
    public void parseResponse(Adventure adventure, Note n, String response)
    {
        System.out.println("Got event response");
        if (!(response.equalsIgnoreCase("pick it up")
                || response.equalsIgnoreCase("leave it")
                || response.equalsIgnoreCase("examine it")))
        {
            super.sendMessage(n, "I'm unsure of how to react to that response. Please try again!~");
        }
        else
        {
            if (response.equalsIgnoreCase("leave it"))
            {
                super.sendMessage(n, "    :asterisk: You compel yourself to leave the sword, and you continue on your" +
                        " adventure.");
                adventure.getResponseFromEvent(this, "ignored");
                super.setCompletedEvent(true);
            }
            else if (response.equalsIgnoreCase("pick it up"))
            {
                if (adventure.getInventory().addItem(new Item("Cracked Steel Sword", "sword", "A sword that fell from" +
                        " the sky", 1)) != -1)
                {
                    super.sendMessage(n, "        :asterisk: You pick up the Sword and store it in your inventory" +
                            ".\n:bulb: `Use _adventure inventory to view your inventory!`");
                    adventure.getResponseFromEvent(this, "stored");
                    adventure.logAction("You picked up an item: Cracked Steel Sword!");
                }
                else
                {
                    super.sendMessage(n, "        :asterisk: You pick up the Sword and attempt to store it, before " +
                            "realising that you have no room for it, so you leave it.\n" + ":bulb: `Use _adventure " +
                            "inventory to view your inventory!`");
                    adventure.getResponseFromEvent(this, "noroom");
                    adventure.logAction("You tried to pick up a Cracked Steel Sword, but you had no room.");
                }
                super.setCompletedEvent(true);
                
            }
            else
            {
                super.sendMessage(n, "        :asterisk: You examine the sword from afar.\n Upon further inspection, you seem to notice that it's a Cracked Steel Sword." + "\n\n** Weapon Information: ** \n ➜ 3 Damage \n ➜ No Bonus Effects" + "\n ➜ *\"A sword that fell from the sky while you were walking\"*");
                adventure.logAction("You examined a sword that fell from the sky.");
            }
        }
    }
}
