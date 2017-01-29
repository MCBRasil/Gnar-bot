package xyz.gnarbot.gnar.commands.executors.polls;

import java.util.HashMap;

public class PollManager {
    private static int pollid = 1;

    private static HashMap<Integer, Poll> polls = new HashMap<>();

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
