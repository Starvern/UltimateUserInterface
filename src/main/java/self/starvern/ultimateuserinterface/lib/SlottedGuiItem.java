package self.starvern.ultimateuserinterface.lib;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.item.ItemConfig;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

/**
 * A GuiItem attached to a slot.
 * @since 0.4.2
 */
public class SlottedGuiItem extends GuiItem
{
    private final int slot;
    private final ItemConfig itemConfig;
    private ItemStack itemStack;

    public SlottedGuiItem(UUI api, GuiItem item, int slot)
    {
        super(api, item.getPage(), item.getId());
        this.slot = slot;
        this.itemStack = super.getItemStack();
        this.itemConfig = super.getItemConfig().copy();
    }

    @Override
    public ItemConfig getItemConfig()
    {
        return this.itemConfig;
    }

    /**
     * @return The ItemStack associated with this GuiItem.
     * @since 0.5.0
     */
    public ItemStack getItemStack()
    {
        ItemStack item = this.itemStack;

        if (!this.actions.isEmpty())
            ItemUtility.addUUID(this.api, item, uuid.toString());

        return item;
    }

    /**
     * Restores and updates the item with placeholders.
     * @param player The player to parse placeholders for.
     * @since 0.5.1
     */
    public void updateItem(OfflinePlayer player)
    {
        this.setItemStack(this.itemConfig.buildItem(player));
        this.loadActions();
        for (GuiAction<GuiItem> action : this.actions)
            action.setArguments(PlaceholderAPIHook.parse(player, action.getArguments()));
    }

    /**
     * Update this SlottedGuiItem's ItemStack
     * @param itemStack The ItemStack to set the item to.
     * @since 0.4.0
     */
    public void setItemStack(ItemStack itemStack)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null && itemMeta.getPersistentDataContainer().has(api.getKey()))
            ItemUtility.removeUUID(this.api, this.itemStack);

        this.itemStack = itemStack;
    }

    /**
     * @return The slot this item appears in.
     * @since 0.5.0
     */
    public int getSlot()
    {
        return this.slot;
    }
}
