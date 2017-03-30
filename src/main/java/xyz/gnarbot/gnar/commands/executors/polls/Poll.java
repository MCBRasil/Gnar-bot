package xyz.gnarbot.gnar.commands.executors.polls;

import xyz.gnarbot.gnar.Bot;

public abstract class Poll {
    private int id;
    private final Bot bot;

    public Poll(Bot bot) {
        this.bot = bot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void start();

    public Bot getBot() {
        return bot;
    }
}
