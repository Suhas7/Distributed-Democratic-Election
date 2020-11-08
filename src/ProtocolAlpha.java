import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ProtocolAlpha {
    //TODO CONCERN: ONLY RELIES ON FIRST CHOICES
    public static String runProtocol(Voter[] voters, int numFaultyVoters) {
        String[] methods = new String[] {"exchangeFirstVote", "setPerplexed", "sendPerplexed", "setAlert"};

        for (int i = 0; i < methods.length; i++) {
            ArrayList<Thread> allThreads = new ArrayList<>();
            final int finalI = i;
            for (int j = 0; j < voters.length; j++) {
                final int finalJ = j;
                Object[][] params = new Object[][] {new Object[] {voters, finalJ}, new Object[] {numFaultyVoters, finalJ}, new Object[] {voters, finalJ}, new Object[] {numFaultyVoters}};
                Thread t = new Thread(() -> {
                    try {
                        Class[] classes = new Class[params[finalI].length];
                        for (int k = 0; k < params[finalI].length; k++) {
                            classes[k] = params[finalI][k].getClass();
                        }

                        Method currMethod = voters[finalJ].getClass().getMethod(methods[finalI], classes);
                        currMethod.invoke(voters[finalJ], params[finalI]);
                    } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                        throw new Error("Error: " + e + " on method: " + methods[finalI] + " because: " + e.getCause() + ", " + e.getLocalizedMessage());
                    }
                });
                allThreads.add(t);
            }

            for (Thread t : allThreads) {
                t.start();
            }

            for (Thread t : allThreads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    throw new Error("Interrupted exception in threads!");
                }
            }
        }

        return reachAgreement(voters);
    }

    //TODO BELIEVE THERE'S AN ISSUE W/ REACH AGREEMENT
    private static String reachAgreement(Voter[] voters) {
        int trueCount = 0;
        for (Voter voter : voters) {
            if (voter.getAlert()) trueCount++;
        }
        if (trueCount > voters.length/2.0) {
            return "don't know";
        } else {
            HashMap<String, Integer> voteCounts = new HashMap<>();
            int currMaxInt = 0;
            String currMaxString = null;

            for (Voter voter : voters) {
                String currVote = voter.getVote();
                if (voteCounts.containsKey(voter.getVote())) {
                    int currAmt = voteCounts.get(currVote);
                    voteCounts.remove(currVote);
                    voteCounts.put(currVote, currAmt+1);

                    if (currAmt+1 > currMaxInt) {
                        currMaxInt = currAmt+1;
                        currMaxString = currVote;
                    } else if (currAmt+1 == currMaxInt) {
                        if (currVote.compareTo(currMaxString) < 0) currMaxString = currVote;
                    }
                } else {
                    voteCounts.put(currVote, 1);
                    if (1 > currMaxInt) {
                        currMaxInt = 1;
                        currMaxString = currVote;
                    } else if (1 == currMaxInt && currVote.compareTo(currMaxString) < 0) {
                        currMaxString = currVote;
                    }
                }
            }
            return currMaxString;
        }
    }

    static class Voter {
        private final String[] myVotes;
        private String[] allVotes;
        private boolean[] perplexedVoters;
        private boolean alert;
        private Object lock;

        public Voter(String[] votes, int numVoters) {
            this.myVotes = votes;
            this.allVotes = new String[numVoters];
            this.perplexedVoters = new boolean[numVoters];
            this.alert = false;
            //this.lock = new Object();
        }

        public void exchangeFirstVote(Voter[] voters, Integer myIdx) {
            for (Voter voter : voters) {
                voter.receiveVote(myVotes[0], myIdx); //sending my vote
            }
        }

        public void receiveVote(String vote, int i) {
            allVotes[i] = vote;
        }

        public void setPerplexed(Integer numFaultyVoters, Integer myIdx) {
            int diff = 0;
            for (String vote : this.allVotes) {
                if (!vote.equals(allVotes[myIdx])) diff++;
            }

            if (diff >= (allVotes.length - numFaultyVoters)/2.0) {
                perplexedVoters[myIdx] = true;
            }
        }

        public void sendPerplexed(Voter[] voters, Integer myIdx) {
            if (perplexedVoters[myIdx]) {
                for (Voter voter : voters) {
                    voter.receivePerplexedMessage(myIdx); //sending "I am perplexed" message
                }
            }
        }

        public void receivePerplexedMessage(int i) {
            perplexedVoters[i] = true;
        }

        public void setAlert(Integer numFaultyVoters) {
            int numPerplexed = 0;
            for (boolean isPerplexed : perplexedVoters) {
                if (isPerplexed) numPerplexed++;
            }
            if (numPerplexed >= allVotes.length - 2 * numFaultyVoters) this.alert = true;
        }

        public boolean getAlert() {
            return this.alert;
        }

        public String getVote() {
            return this.myVotes[0];
        }
    }
}