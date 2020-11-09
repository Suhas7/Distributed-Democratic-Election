import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;

public class TestProtocolAlpha {
    @Test
    public void testNoFaultyVoters() {
        final int NUM_VOTERS = 5;
        Voter[] voters = new Voter[NUM_VOTERS];
        voters[0] = new Voter(new String[]{"a", "b", "c"}, NUM_VOTERS, 0);
        voters[1] = new Voter(new String[]{"a", "b", "c"}, NUM_VOTERS, 1);
        voters[2] = new Voter(new String[]{"b", "a", "c"}, NUM_VOTERS, 2);
        voters[3] = new Voter(new String[]{"b", "a", "c"}, NUM_VOTERS, 3);
        voters[4] = new Voter(new String[]{"a", "a", "c"}, NUM_VOTERS, 4);

        Assert.assertEquals("a", ProtocolAlpha.runProtocol(voters, 0));
    }

    @Test
    public void test2FaultyVoters() {
        final int NUM_VOTES = 7;
        Voter[] voters = new Voter[NUM_VOTES];
        voters[0] = new Voter(new String[]{"b", "a", "c"}, NUM_VOTES, 0);
        voters[1] = new Voter(new String[]{"b", "a", "c"}, NUM_VOTES, 1);
        voters[2] = new Voter(new String[]{"b", "a", "c"}, NUM_VOTES, 2);
        voters[3] = new Voter(new String[]{"c", "b", "a"}, NUM_VOTES, 3);
        voters[4] = new Voter(new String[]{"c", "a", "b"}, NUM_VOTES, 4);
        voters[5] = new Voter(new String[]{"c", "a", "b"}, NUM_VOTES, 5);
        voters[6] = new Voter(new String[]{"c", "a", "b"}, NUM_VOTES, 6);

        Assert.assertEquals("a", ProtocolAlpha.runProtocol(voters, 2));
    }

//    @Test
//    public void testNoFaultyVoters2ValuesWithEqualVotes() {
//        Voter[] voters = new Voter[5];
//        voters[0] = new Voter(new String[]{"a", "b", "c"}, 5);
//        voters[1] = new Voter(new String[]{"a", "b", "c"}, 5);
//        voters[2] = new Voter(new String[]{"b", "b", "c"}, 5);
//        voters[3] = new Voter(new String[]{"b", "b", "c"}, 5);
//        voters[4] = new Voter(new String[]{"c", "a", "c"}, 5);
//
//        Assert.assertEquals("a", ProtocolAlpha.runProtocol(voters, 1));
//    }
}
