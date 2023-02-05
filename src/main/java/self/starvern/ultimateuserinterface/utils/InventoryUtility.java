package self.starvern.ultimateuserinterface.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtility
{
    /**
     * @param inventory The inventory
     * @param item The item
     * @return The slot of the item, or -1 if not found.
     * @since 0.2.3
     */
    public static int getSlot(Inventory inventory, ItemStack item)
    {
        for (int i = 0; i < inventory.getContents().length; i++)
        {
            if (item.equals(inventory.getContents()[i]))
            {
                return i;
            }
        }
        return -1;
    }
}
