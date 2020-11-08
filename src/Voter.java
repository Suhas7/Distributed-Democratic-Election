public class Voter {
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

    public void proposeTopChoice(Voter[] voters, Integer myIdx) {
        for (Voter voter : voters) {
            voter.receiveVote(myVotes[0], myIdx); //sending my vote
        }
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

    public void setAlert(Integer numFaultyVoters) {
        int numPerplexed = 0;
        for (boolean isPerplexed : perplexedVoters) {
            if (isPerplexed) numPerplexed++;
        }
        if (numPerplexed >= allVotes.length - 2 * numFaultyVoters) this.alert = true;
    }

    public void receiveVote(String vote, int i) {
        allVotes[i] = vote;
    }

    public void receivePerplexedMessage(int i) {
        perplexedVoters[i] = true;
    }

    public boolean getAlert() {
        return this.alert;
    }

    public String getVote() {
        return this.myVotes[0];
    }
}
