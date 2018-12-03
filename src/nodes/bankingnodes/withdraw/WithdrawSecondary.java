package nodes.bankingnodes.withdraw;

import nfa.Edge;
import nodes.creationNodes.AFKCreation;
import nodes.creationNodes.HoverBankerCreation;
import nodes.creationNodes.PrematureStopCreation;
import org.osbot.rs07.Bot;
import scriptclasses.TodaysRecipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WithdrawSecondary extends Withdraw {
    private List<Edge> stillNeedPrimary = Collections.singletonList(
            new Edge(WithdrawPrimary.class, 1));

    private List<Edge> readyToCreate = Arrays.asList(
            new Edge(AFKCreation.class, 100),
            new Edge(HoverBankerCreation.class, 50),
            new Edge(PrematureStopCreation.class, 10));

    public WithdrawSecondary(Bot bot) {
        super(bot);
    }
    @Override
    public boolean canExecute() throws InterruptedException {
        return !inventory.contains(TodaysRecipe.getSecondaryID())
                || inventory.isEmpty();
    }

    @Override
    boolean withdrawItem() {
        return bank.withdraw(TodaysRecipe.getSecondaryID(), 14);
    }

    @Override
    public List<Edge> getAdjacentNodes() {
        boolean hasSecondary = inventory.contains(TodaysRecipe.getPrimaryID(), TodaysRecipe.getSecondaryID());
        return hasSecondary ? readyToCreate : stillNeedPrimary;
    }
}
