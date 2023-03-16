package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.DragType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;

import java.util.Optional;
import java.util.Set;

/**
 * <p>
 *     Unlike the InventoryDragEvent, this will only fire when a stack of items
 *     are distributed to slots, not when an item is moved to another slot.
 * </p>
 */
public class GuiDragEvent extends GuiEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private final Set<GuiItem> items;

    private final DragType dragType;
    private final ItemStack oldCursor;
    private final ItemStack newCursor;

    private boolean cancel;

    public GuiDragEvent(@NotNull HumanEntity human, @NotNull GuiPage page, @NotNull DragType dragType,
                        @NotNull Set<GuiItem> items, ItemStack oldCursor, ItemStack newCursor)
    {
        super(human, page);
        this.items = items;
        this.dragType = dragType;
        this.newCursor = newCursor;
        this.oldCursor = oldCursor;
    }

    /**
     * @return All GuiItems in which were changed during this event.
     */
    public Set<GuiItem> getItems()
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
