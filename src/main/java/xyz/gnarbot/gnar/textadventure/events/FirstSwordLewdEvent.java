package xyz.gnarbot.gnar.textadventure.events;

import net.dv8tion.jda.core.entities.Message;
import xyz.gnarbot.gnar.textadventure.Adventure;
import xyz.gnarbot.gnar.textadventure.Event;
import xyz.gnarbot.gnar.textadventure.Item;

public class FirstSwordLewdEvent extends Event {

    public FirstSwordLewdEvent() {
        super("FirstDildo", "First Dildo", "Get your first Dildo");
    }

    @Override
    public String getEventID() {
        return super.getEventID();
    }

    @Override
    public String getEventName() {
        return "First Dildo";
    }

    @Override
    public String getEventDescription() {
        return "You found a Dildo on the floor!";
    }

    @Override
    public boolean hasCompletedEvent() {
        return super.hasCompletedEvent();
    }

    @Override
    public Event runEvent(Adventure adventure, Message msg) {
        super.sendMessage(msg, ":bulb: " + "Resting, you suddenly notice a dildo on the ground.\n" + "However, in " +
                "these" + " woods, there's no one around.\n" + "Yet, your heart throbs, the urge to do so is profound" +
                ".\n" + "Will your pride be renowned?" + "\n   :warning: What would you like to do?\n  " + "➜ `Pick " +
                "it up`\n " + " ➜ `Leave it`\n  ➜ `Examine it`\n  ➜ `Use it`\n \n " + ":bulb: `Use the _adventure " +
                "command to select" + " a response! Example: _adventure Pick it up`");
        return this;
    }

    @Override
    public void parseResponse(Adventure adventure, Message msg, String response) {
        System.out.println("Got event response");
        if (!(response.equalsIgnoreCase("pick it up") || response.equalsIgnoreCase("leave it") || response
                .equalsIgnoreCase("examine it") || response
                .equalsIgnoreCase("use it"))) {
            super.sendMessage(msg, "I'm unsure of how to react to that response. Please try again!~");
        } else {
            if (response.equalsIgnoreCase("leave it")) {
                super.sendMessage(msg, "    :asterisk: You compel yourself to leave the Dildo, even though it looks " +
                        "massive and you want to try it, and you continue on your adventure.");
                adventure.getResponseFromEvent(this, "ignored");
                super.setCompletedEvent(true);
            } else if (response.equalsIgnoreCase("use it")) {
                adventure.sendMessage(msg, "    :warning: You slowly creep towards it... with your heart beating " +
                        "faster" + ".\n" + " You start to notice that your legs have been trembling ever since laying" +
                        " your eyes " + "on it's shaft.\n" + " You poke it... breathing heavily. You notice it has a " +
                        "slight wobble, " + "despite it being made of steel.\n" + " You hear a voice calling towards " +
                        "you *\"Ussseeee ittt" + ". Usseeee itt! Use it and become one with me!\"*\n" + " You look " +
                        "around, seeing nobody, " + "before lunging towards it, inserting it's long, cold but " +
                        "pleasing shaft into you.\n" + " " + ":alarm_clock: *Time passes* You finally faint from the " +
                        "exhaustion of using it over and over " + "again...");
                super.sendMessage(msg, ":bulb: " + "Resting from using the dildo, you suddenly see it shine.\n" +
                        "Still," + " in these woods, there's no one around.\n" + "Yet, your heart throbs, the urge to" +
                        " do " + "is so profound.\n" + "Will your pride be once again renowned?" + "\n   :warning: " +
                        "What would " + "you like to do?\n  " + "➜ `Pick it up`\n  ➜ `Leave it`\n  ➜ `Examine it`\n  " +
                        "➜ `Use it`\n \n " + "" + ":bulb: `Use the _adventure command to select a response! Example: " +
                        "_adventure Pick it " + "up`");
            } else if (response.equalsIgnoreCase("pick it up")) {
                if (adventure.getInventory()
                        .addItem(new Item("Cracked Steel Dildo", "Dildo", "A Dildo that fell from" + " the sky", 1))
                        != -1) {
                    super.sendMessage(msg, "        :asterisk: You pick up the Dildo and store it in your inventory" +
                            ".\n:bulb: `Use _adventure inventory to view your inventory!`");
                    adventure.getResponseFromEvent(this, "stored");
                    adventure.logAction("You picked up an item: Cracked Steel Dildo!");
                } else {
                    super.sendMessage(msg, "        :asterisk: You pick up the Dildo and attempt to store it, before "
                            + "realising that you have no room for it, so you leave it.\n" + ":bulb: `Use _adventure " +
                            "" + "inventory to view your inventory!`");
                    adventure.getResponseFromEvent(this, "noroom");
                    adventure.logAction("You tried to pick up a Cracked Steel Dildo, but you had no room.");
                }
                super.setCompletedEvent(true);

            } else {
                super.sendMessage(msg, "        :asterisk: You examine the Dildo from afar.\n Upon further inspection, " +
                        "you seem to notice that it's a Cracked Steel Dildo." + "\n\n** Weapon Information: ** \n ➜ 3" +
                        " Sex Drive \n ➜ No Bonus Effects" + "\n ➜ *\"A Dildo that fell from the sky while you were " +
                        "walking\"*");
                adventure.logAction("You examined a Dildo that fell from the sky.");
            }
        }
    }
}
