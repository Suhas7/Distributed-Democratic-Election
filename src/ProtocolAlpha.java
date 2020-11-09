import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ProtocolAlpha {
    //TODO CONCERN: ONLY RELIES ON FIRST CHOICES

    //TODO currently only consider the voters where alert=false
    //TODO error when all alert = false (i.e. no majority)
    //TODO currently return empty string because idk what the algo does there
    public static String runProtocol(Voter[] voters, int numFaultyVoters) {
        ArrayList<Thread> allThreads;

        //Step 1: For each voter, exchange first vote (done in Standard Byzantine Agreement)
        //Step 2: Agree on votes -- Standard Byzantine Agreement
        for (int kingIdx = 1; kingIdx <= numFaultyVoters + 1; kingIdx++) {
            //First phase
            allThreads = new ArrayList<>();
            for (Voter voter : voters) {
                Thread t = new Thread(() -> {
                    voter.sendVote(voters);
                });
                allThreads.add(t);
            }
            threadCleanUp(allThreads);

            allThreads = new ArrayList<>();
            for (Voter voter : voters) {
                Thread t = new Thread(() -> {
                    voter.setMyValue();
                });
                allThreads.add(t);
            }
            threadCleanUp(allThreads);

            //Second phase
            voters[kingIdx-1].sendKingValue(voters);

            allThreads = new ArrayList<>();
            for (Voter voter : voters) {
                Thread t = new Thread(() -> {
                    voter.chooseMyOrKingVal(numFaultyVoters);
                });
                allThreads.add(t);
            }
            threadCleanUp(allThreads);
        }

        ArrayList<String> finalVotes = new ArrayList<>();
        allThreads = new ArrayList<>();
        for (Voter voter : voters) {
            Thread t = new Thread(() -> {
                finalVotes.add(voter.getFinalVote());
            });
            allThreads.add(t);
        }
        threadCleanUp(allThreads);

        return findMajority(finalVotes);
    }

    private static void threadCleanUp(ArrayList<Thread> threads) {
        startThreads(threads);
        joinThreads(threads);
    }

    private static void startThreads(ArrayList<Thread> threads) {
        for (Thread t : threads) t.start();
    }

    private static void joinThreads(ArrayList<Thread> threads) {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new Error("Interrupted exception in threads!");
            }
        }
    }

    private static String findMajority(ArrayList<String> votes) {
        HashMap<String, Integer> voteCount = new HashMap<>();
        int maxInt = 0;
        String maxString = null;

        for (String vote : votes) {
            int newAmt = 1;
            if (voteCount.containsKey(vote)) {
                newAmt = voteCount.get(vote) + 1;
                voteCount.remove(vote);
            }

            voteCount.put(vote, newAmt);
            if (newAmt > maxInt) {
                maxInt = newAmt;
                maxString = vote;
            } else if (newAmt == maxInt) {
                if (vote.compareTo(maxString) < 0) maxString = vote;
            }
        }

        return maxString;
    }
}