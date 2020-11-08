import org.junit.Test;
import org.junit.Assert;

public class TestProtocolAlpha {
    @Test
    public void testNoFaultyVoters() {
        ProtocolAlpha.Voter[] voters = new ProtocolAlpha.Voter[5];
        voters[0] = new ProtocolAlpha.Voter(new String[]{"a", "b", "c"}, 5);
        voters[1] = new ProtocolAlpha.Voter(new String[]{"a", "b", "c"}, 5);
        voters[2] = new ProtocolAlpha.Voter(new String[]{"b", "b", "c"}, 5);
        voters[3] = new ProtocolAlpha.Voter(new String[]{"b", "b", "c"}, 5);
        voters[4] = new ProtocolAlpha.Voter(new String[]{"a", "a", "c"}, 5);

        Assert.assertEquals(ProtocolAlpha.runProtocol(voters, 0), "a");
    }
}
