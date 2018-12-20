package nodes.banking_nodes;


import nfa.Edge;
import nfa.ExecutableNode;
import nodes.banking_nodes.withdraw.WithdrawPrimary;
import nodes.banking_nodes.withdraw.WithdrawSecondary;
import org.osbot.rs07.Bot;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Option;
import org.osbot.rs07.input.mouse.EntityDestination;
import org.osbot.rs07.input.mouse.RectangleDestination;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

import java.util.Arrays;
import java.util.List;

public class DepositNode extends MethodProvider implements ExecutableNode {
    private NPC lastUsedBanker;
    private List<Edge> adjNodes = Arrays.asList(
            new Edge(WithdrawPrimary.class, 50),
            new Edge(WithdrawSecondary.class, 50)
    );

    public DepositNode(Bot bot){
        exchangeContext(bot);
    }

    @Override
    public boolean canExecute() {
        if(lastUsedBanker == null){
            lastUsedBanker = npcs.closestThatContains("Banker");
        }
        return lastUsedBanker.exists();
    }

    @Override
    public int executeNode() {
        boolean open = new ConditionalSleep(5000) {
            @Override
            public boolean condition() throws InterruptedException {
                return bank.isOpen() || rightClickOpenBank();
            }
        }.sleep();
        if(open){
            if(!inventory.isEmpty()){
                boolean deposited = new ConditionalSleep(5000) {
                    @Override
                    public boolean condition() throws InterruptedException {
                        return bank.depositAll();
                    }
                }.sleep();
                if(deposited){
                    return randomNormalDist(500, 250);
                }
            }

        }
        return 0;
    }

    private boolean rightClickOpenBank() throws InterruptedException {
        widgets.getWidgets().closeOpenInterface();
        return hoverOverBankOption() && mouse.click(false);
    }

    private boolean hoverOverBankOption() throws InterruptedException {
        boolean success = false;
        boolean found = false;
        int idx = 0;
        int attempts = 0;
        while(!found && attempts++ < 3){
            if(mouse.click(new EntityDestination(bot, lastUsedBanker), true)){
                if(menu.isOpen()){
                    List<Option> options = menu.getMenu();
                    for(; idx < options.size(); idx++){
                        if(options.get(idx).action.equals("Bank")){
                            found = true;
                            break;
                        }
                    }
                }
            }
        }
        if(found){
            RectangleDestination bankOptionRect = new RectangleDestination(bot, menu.getOptionRectangle(idx));
            success = mouse.move(bankOptionRect);
        }
        return success;
    }

    @Override
    public List<Edge> getAdjacentNodes() {
        return adjNodes;
    }

    @Override
    public void logNode() {
        log(this.getClass().getSimpleName());
    }
}
