package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.DragType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *     Unlike the InventoryDragEvent, this will only fire when a stack of items
 *     is distributed to slots, not when an item is moved to another slot.
 * </p>
 * @since 0.4.2
 */
public class GuiDragEvent extends GuiEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private final List<SlottedGuiItem> items;

    private final DragType dragType;
    private final ItemStack oldCursor;
    private final ItemStack newCursor;
    private final Map<Integer, ItemStack> newItems;

    private boolean cancel;

    public GuiDragEvent(@NotNull HumanEntity human, @NotNull GuiPage page, @NotNull DragType dragType,
                        @NotNull List<SlottedGuiItem> items, Map<Integer, ItemStack> newItems, ItemStack oldCursor,
                        ItemStack newCursor)
    {
        super(human, page);
        this.items = items;
        this.newItems = newItems;
        this.dragType = dragType;
        this.newCursor = newCursor;
        this.oldCursor = oldCursor;
    }

    /**
     * @return All GuiItems in which were changed during this event.
     * @since 0.4.2
     */
    public List<SlottedGuiItem> getItems()
    {
        return this.items;
    }

    public DragType getDragType()
    {
        return dragType;
    }

    public ItemStack getOldCursor()
    {
        return oldCursor;
    }

    public ItemStack getNewCursor()
    {
        return newCursor;
    }

    public void setCursor(ItemStack itemStack)
    {
        super.getHuman().getItemOnCursor().setItemMeta(itemStack.getItemMeta());
    }

    public Map<Integer, ItemStack> getNewItems()
    {
        return newItems;
    }

    @NotNull
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    @Override
    public boolean isCancelled()
    {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel)
    {
        this.cancel = cancel;
    }

    @NotNull
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
