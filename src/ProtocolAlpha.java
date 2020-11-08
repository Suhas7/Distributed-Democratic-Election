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
        String[] methods = new String[] {"proposeTopChoice", "handlePerplexed"};

        for (int i = 0; i < methods.length; i++) {
            ArrayList<Thread> allThreads = new ArrayList<>();
            final int finalI = i;
            for (int j = 0; j < voters.length; j++) {
                final int finalJ = j;
                Object[][] params = new Object[][] {new Object[] {voters, finalJ}, new Object[] {voters, numFaultyVoters, finalJ}};
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

        ArrayList<String> votes = new ArrayList<>();
        for (Voter voter : voters) {
            String vote = voter.sendVote(numFaultyVoters);
            if (vote != null) votes.add(vote);
        }
        System.out.println(votes);

        return votes.size() > 0 ? votes.get(0) : "";
    }
}