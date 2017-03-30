package xyz.gnarbot.gnar.commands.executors.polls;

import java.util.HashMap;

public class PollManager {
    private static final HashMap<Integer, Poll> polls = new HashMap<>();
    private static int idCount = 0;

    public static void registerPoll(Poll p) {
        int id = idCount++;
        p.setId(id);
        p.start();
        polls.put(id, p);
    }

    public static Poll getPollFromId(int id) {
        if (polls.containsKey(id)) {
            return polls.get(id);
        }
        return null;
    }

}
