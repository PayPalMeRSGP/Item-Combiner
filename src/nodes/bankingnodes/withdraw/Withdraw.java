package nodes.bankingnodes.withdraw;

import nfa.Edge;
import nfa.ExecutableNode;
import nodes.creationNodes.AFKCreation;
import nodes.creationNodes.HoverBankerCreation;
import nodes.creationNodes.PrematureStopCreation;
import org.osbot.rs07.Bot;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.script.MethodProvider;
import scriptclasses.TodaysRecipe;

import java.util.Arrays;
import java.util.List;

public abstract class Withdraw extends MethodProvider implements ExecutableNode {

    private List<Edge> bothItemsWithdrawn = Arrays.asList(
            new Edge(AFKCreation.class, 100),
            new Edge(HoverBankerCreation.class, 50),
            new Edge(PrematureStopCreation.class, 10));

    Withdraw(Bot bot) {
        exchangeContext(bot);
    }

    @Override
    public int executeNode() throws InterruptedException {
        if(bank.open() && bank.enableMode(Bank.BankMode.WITHDRAW_ITEM)){
            if(outOfMaterials()){
                log("Stopping due to item shortages");
                return -1;
            }
            if(withdrawItem())
                return randomNormalDist(500, 100);
        }
        return 0;
    }

    private boolean outOfMaterials() {
        return bank.getAmount(TodaysRecipe.getPrimaryID(), TodaysRecipe.getSecondaryID()) < 28;
    }

    abstract boolean withdrawItem();

    @Override
    public void logNode() {
        log(this.getClass().getSimpleName());
    }
}
