package nodes.creationNodes;

import org.osbot.rs07.Bot;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;
import scriptclasses.TodaysRecipe;

/*
While items are combining, right click hover the bank's open option.
Before all items have combined, prematurely open the bank.
Emulate a human mistiming item combination speed, over eager to grind his virtual levels, and opening the bank too fast.
Guess you won't be winning any efficiency scape EHP competitions.
 */
public class PrematureStopCreation extends HoverBankerCreation { //extend HoverBank to get hoverOverBankOption() method

    private int secondaryCount;
    private int primaryCount;

    public PrematureStopCreation(Bot bot) {
        super(bot);
    }

    @Override
    public boolean canExecute() {
        secondaryCount = (int) inventory.getAmount(TodaysRecipe.getSecondaryID());
        primaryCount = (int) inventory.getAmount(TodaysRecipe.getPrimaryID());
        return super.canExecute();
    }

    @Override
    int waitForPotions() throws InterruptedException {
        MethodProvider.sleep(randomNormalDist(3000, 1000));
        boolean hovered = hoverOverBankOption();

        //find the ID of the item you have the fewest of in your inventory. Prematurely bank when there is one of that item left
        int limitingItemID = primaryCount > secondaryCount ? TodaysRecipe.getSecondaryID() : TodaysRecipe.getPrimaryID();
        new ConditionalSleep(25000) {
            @Override
            public boolean condition() {
                return inventory.getAmount(limitingItemID) <= 1; //stop when there is about 1 item left to combine.
            }
        }.sleep();

        if(hovered){
            mouse.click(false);
        }
        return randomNormalDist(1200, 200);
    }
}
