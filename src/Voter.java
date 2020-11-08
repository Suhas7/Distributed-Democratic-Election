import java.util.HashMap;

public class Voter {
    private final String[] myVotes;
    private String[] allVotes;
    private boolean[] perplexedVoters;
    private boolean alert;
//    private Object lock;

    public Voter(String[] votes, int numVoters) {
        this.myVotes = votes;
        this.allVotes = new String[numVoters];
        this.perplexedVoters = new boolean[numVoters];
        this.alert = false;
        //this.lock = new Object();
    }

    public void proposeTopChoice(Voter[] voters, Integer myIdx) {
        for (Voter voter : voters) {
            voter.receiveVote(myVotes[0], myIdx); //sending my vote
        }
    }

    public void handlePerplexed(Voter[] voters, Integer numFaultyVoters, Integer myIdx) {
        int diff = 0;
        for (String vote : this.allVotes) {
            if (!vote.equals(myVotes[0])) diff++;
        }

        if (diff >= (allVotes.length - numFaultyVoters)/2.0) { //am perplexed
            sendPerplexed(voters, myIdx);
        }
    }

    public String sendVote(int numFaultyVoters) {
        int numPerplexed = 0;
        for (boolean isPerplexed : perplexedVoters) {
            if (isPerplexed) numPerplexed++;
        }
        if (numPerplexed >= allVotes.length - 2 * numFaultyVoters) { //alert == true
            this.alert = true;
        } else {
            HashMap<String, Integer> voteCounts = new HashMap<>();
            int currMaxInt = 0;
            String currMaxString = null;

            for (int i = 0; i < perplexedVoters.length; i++) {
                if (!perplexedVoters[i]) {
                    String currVote = allVotes[i];
                    if (voteCounts.containsKey(currVote)) {
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
            }

            return (currMaxString == null) ? "" : currMaxString;
        }
        return null;
    }

    public void eliminateUnqualified() {

    }

    public void receiveVote(String vote, int i) {
        allVotes[i] = vote;
    }

    private void sendPerplexed(Voter[] voters, Integer myIdx) {
        for (Voter voter : voters) {
            voter.receivePerplexedMessage(myIdx); //sending "I am perplexed" message
        }
    }

    public void receivePerplexedMessage(int i) {
        perplexedVoters[i] = true;
    }

    public boolean getAlert() {
        return this.alert;
    }

    public String getVote() {
        if (this.alert) {

        }
        return "";
    }
}
