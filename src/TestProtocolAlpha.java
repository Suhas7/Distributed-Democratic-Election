import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;

public class TestProtocolAlpha {
    @Test
    public void testNoFaultyVoters() {
        Voter[] voters = new Voter[5];
        voters[0] = new Voter(new String[]{"a", "b", "c"}, 5);
        voters[1] = new Voter(new String[]{"a", "b", "c"}, 5);
        voters[2] = new Voter(new String[]{"b", "b", "c"}, 5);
        voters[3] = new Voter(new String[]{"b", "b", "c"}, 5);
        voters[4] = new Voter(new String[]{"a", "a", "c"}, 5);

        Assert.assertEquals("a", ProtocolAlpha.runProtocol(voters, 0));
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
