package scriptclasses;

import org.osbot.rs07.api.model.Item;

public class TodaysRecipe {
    private static Item primary, secondary;

    public static void setItemCombination(Item A, Item B) {
        primary = A;
        secondary = B;
    }

    public static int getPrimaryID() {
        return primary.getId();
    }

    public static int getSecondaryID() {
        return secondary.getId();
    }

    public static void nullifyStatics() {
        primary = null;
        secondary = null;
    }


}
