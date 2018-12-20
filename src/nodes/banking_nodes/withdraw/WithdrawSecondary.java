package nodes.banking_nodes.withdraw;

import nfa.Edge;
import nodes.creation_nodes.AFKCreation;
import nodes.creation_nodes.HoverBankerCreation;
import nodes.creation_nodes.PrematureStopCreation;
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
        boolean hasPrimary = inventory.contains(TodaysRecipe.getPrimaryID());
        return hasPrimary ? readyToCreate : stillNeedPrimary;
    }
}
