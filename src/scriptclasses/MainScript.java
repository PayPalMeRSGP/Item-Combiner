package scriptclasses;

import nfa.MarkovNodeExecutor;
import nodes.bankingnodes.DepositNode;
import nodes.bankingnodes.withdraw.WithdrawPrimary;
import nodes.bankingnodes.withdraw.WithdrawSecondary;
import nodes.creationNodes.AFKCreation;
import nodes.creationNodes.HoverBankerCreation;
import nodes.creationNodes.PrematureStopCreation;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.listener.MessageListener;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "PayPalMeRSGP", name = MainScript.SCRIPT_NAME, info = "Combines any 2 items for xp or gp.", version = 1.0, logo = "")
public class MainScript extends Script implements MessageListener {
    static final String SCRIPT_NAME = "Item_Combinator";
    private MarkovNodeExecutor executor;

    @Override
    public void onStart() throws InterruptedException {
        super.onStart();
        setCombinationRecipe();
        new Paint(bot);
        executor = new MarkovNodeExecutor(
                new WithdrawPrimary(bot),
                new WithdrawSecondary(bot),
                new DepositNode(bot),
                new AFKCreation(bot),
                new HoverBankerCreation(bot),
                new PrematureStopCreation(bot)
        );
        camera.movePitch(67); //move camera as far up as possible, less players in menu when right clicking
    }

    @Override
    public int onLoop() throws InterruptedException {
        int sleepTime = executor.executeThenTraverse();
        if(sleepTime < 0){ //if any node returns -1, its time to stop script
            stop(false);
            return 5000;
        } else return sleepTime;
    }

    @Override
    public void onExit() throws InterruptedException {
        super.onExit();
    }

    //determine from inventory what the session's recipe is.
    private void setCombinationRecipe() {
        Item[] items = inventory.getItems();
        Item primary = null;
        Item secondary = null;
        for (Item item : items) {
            if(item != null) {
                if(primary == null) {
                    primary = item;
                } else if(!primary.equals(item)) {
                    secondary = item;
                    break;
                }
            }
        }
        if(primary != secondary && secondary != null) {
            TodaysRecipe.setItemCombination(primary, secondary);
            log("today's recipe is: " + primary.getName() + " + " + secondary.getName());
        } else {
            log("ERROR: unable to set combination recipe! Make sure that only 2 items are present in the inventory");
            stop(false);
        }
    }

    @Override
    public void onMessage(Message message) throws InterruptedException {
        if(message.getType() == Message.MessageType.GAME) {
            if(message.getMessage().contains("Nothing interesting happens")){
                log("Items not combinable!");
                stop(false);
            }
        }
    }
}
