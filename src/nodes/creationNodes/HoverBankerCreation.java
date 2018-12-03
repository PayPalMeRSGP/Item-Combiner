package nodes.creationNodes;

import org.osbot.rs07.Bot;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Option;
import org.osbot.rs07.input.mouse.EntityDestination;
import org.osbot.rs07.input.mouse.RectangleDestination;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;
import scriptclasses.TodaysRecipe;

import java.util.List;

/*
While items are combining, right click hover the bank's open option.
When items are combined, click open
 */
public class HoverBankerCreation extends AbstractCreationNode {
    private NPC lastHoveredBanker;

    public HoverBankerCreation(Bot bot) {
        super(bot);
    }

    @Override
    int waitForPotions() throws InterruptedException {
        MethodProvider.sleep(randomNormalDist(5000, 2500));
        boolean hovered = hoverOverBankOption();
        new ConditionalSleep(25000) {
            @Override
            public boolean condition() throws InterruptedException {
                return !inventory.contains(TodaysRecipe.getPrimaryID()) || !inventory.contains(TodaysRecipe.getSecondaryID());
            }
        }.sleep();

        if(hovered){
            mouse.click(false);
        }
        return randomNormalDist(1200, 200);
    }

    boolean hoverOverBankOption(){
        NPC banker = (lastHoveredBanker == null || !lastHoveredBanker.exists()) ? npcs.closest("Banker") : lastHoveredBanker;
        boolean success = false;
        if(banker != null){
            lastHoveredBanker = banker;
            boolean found = false;
            int idx = 0;
            int attempts = 0;
            while(!found && attempts++ < 5){
                if(mouse.click(new EntityDestination(bot, banker), true)){
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
        }
        return success;
    }
}
