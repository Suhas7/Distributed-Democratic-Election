import java.util.ArrayList;
import java.util.HashMap;

public class Voter {
    private final String[] myVotes;
    private String[] allVotes;
    private boolean[] perplexedVoters;
    private boolean alert;

    private int myVoterIdx;
    private String kingValue;
//    private int currIdx;
//    private Object lock;

    public Voter(String[] votes, int numVoters, int myVoterIdx) {
        this.myVotes = votes;
        this.allVotes = new String[numVoters];
        this.allVotes[myVoterIdx] = myVotes[0];
        this.perplexedVoters = new boolean[numVoters];
        this.alert = false;
        this.myVoterIdx = myVoterIdx;
        this.kingValue = null;
    }

    public void sendVote(Voter[] voters) {
        for (Voter voter : voters) {
            voter.receiveVote(allVotes[myVoterIdx], myVoterIdx); //sending my vote
        }
    }

    public void setMyValue() {
        HashMap<String, Integer> voteCount = new HashMap<>();
        int maxInt = 0;
        String maxString = null;
        boolean isHardMajority = true;

        for (String vote : allVotes) {
            int newAmt = 1;
            if (voteCount.containsKey(vote)) {
                newAmt = voteCount.get(vote) + 1;
                voteCount.remove(vote);
            }

            voteCount.put(vote, newAmt);
            if (newAmt > maxInt) {
                maxInt = newAmt;
                maxString = vote;
                isHardMajority = true;
            } else if (newAmt == maxInt) isHardMajority = false;
        }

        if (isHardMajority) allVotes[myVoterIdx] = maxString;
    }

    public void sendKingValue(Voter[] voters) {
        for (Voter voter : voters) {
            voter.receiveKingVote(allVotes[myVoterIdx]);
        }
    }

    public void chooseMyOrKingVal(int numFaultyVoters) {
        int myValueCopies = 0;
        for (String vote : allVotes) {
            if (vote.equals(allVotes[myVoterIdx])) myValueCopies++;
        }

        if (myValueCopies <= (allVotes.length/2 + numFaultyVoters)) {
            allVotes[myVoterIdx] = kingValue;
        }
    }

    public String getFinalVote() {
        return allVotes[myVoterIdx];
    }

//    public void eliminateUnqualified() {
//
//    }

    //HELPER FUNCTIONS

    private void receiveVote(String vote, int i) {
        if (i != myVoterIdx) this.allVotes[i] = vote;
    }

    private void receiveKingVote(String vote) {
        this.kingValue = vote;
    }

//    private void sendPerplexed(Voter[] voters) {
//        for (Voter voter : voters) {
//            voter.receivePerplexedMessage(myVoterIdx); //sending "I am perplexed" message
//        }
//    }
//
//    private void receivePerplexedMessage(int i) {
//        perplexedVoters[i] = true;
//    }
//
//    private String getMajorityVote() {
//        HashMap<String, Integer> voteCounts = new HashMap<>();
//        int currMaxInt = 0;
//        String currMaxString = null;
//
//        for (int i = 0; i < perplexedVoters.length; i++) {
//            if (!perplexedVoters[i]) {
//                String currVote = allVotes[i];
//                if (voteCounts.containsKey(currVote)) {
//                    int currAmt = voteCounts.get(currVote);
//                    voteCounts.remove(currVote);
//                    voteCounts.put(currVote, currAmt+1);
//
//                    if (currAmt+1 > currMaxInt) {
//                        currMaxInt = currAmt+1;
//                        currMaxString = currVote;
//                    } else if (currAmt+1 == currMaxInt) {
//                        if (currVote.compareTo(currMaxString) < 0) currMaxString = currVote;
//                    }
//                } else {
//                    voteCounts.put(currVote, 1);
//                    if (1 > currMaxInt) {
//                        currMaxInt = 1;
//                        currMaxString = currVote;
//                    } else if (1 == currMaxInt && currVote.compareTo(currMaxString) < 0) {
//                        currMaxString = currVote;
//                    }
//                }
//            }
//        }
//        return currMaxString != null ? currMaxString : "";
//    }
}
