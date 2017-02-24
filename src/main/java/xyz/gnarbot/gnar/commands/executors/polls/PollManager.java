package xyz.gnarbot.gnar.commands.executors.polls;

import java.util.HashMap;

public class PollManager {
    private static final HashMap<Integer, Poll> polls = new HashMap<>();
    private static int pollid = 1;

    public static void registerPoll(Poll p) {
        int id = pollid++;
        p.setPollid(id);
        p.startPoll();
        polls.put(id, p);
    }

    public static Poll getPollFromId(int id) {
        if (polls.containsKey(id)) {
            return polls.get(id);
        }
        return null;
    }

}
