package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;

import java.util.Optional;

/**
 * <p>
 *     Called when:
 *     1) A GuiItem is directly clicked.
 *     2) An item is shift clicked into a GuiItem's slot (only if the GuiItem is AIR)
 *     3) An item is stacked in a double click item collection
 * </p>
 * @since 0.4.2
 */
public class GuiClickEvent extends GuiEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private final GuiItem item;
    private final ClickType clickType;
    private final boolean outside;
    private final ItemStack itemStack;
    private final ItemStack cursor;
    private final InventoryAction action;
    private final GuiClickType type;

    private boolean cancel;

    public GuiClickEvent(@NotNull HumanEntity human, @NotNull GuiPage page, @NotNull ClickType clickType,
                         @Nullable SlottedGuiItem item, @Nullable ItemStack itemStack, @Nullable ItemStack cursor,
                         InventoryAction action, GuiClickType type, boolean outside)
    {
        super(human, page);
        this.item = item;
        this.clickType = clickType;
        this.outside = outside;
        this.itemStack = itemStack;
        this.cursor = cursor;
        this.action = action;
        this.type = type;
    }

    public Optional<GuiItem> getItem()
    {
        if (item == null)
            return Optional.empty();
        return Optional.of(item);
    }

    @Nullable
    public ItemStack getItemStack()
    {
        return itemStack;
    }

    @Nullable
    public ItemStack getCursor()
    {
        return cursor;
    }

    public InventoryAction getAction()
    {
        return action;
    }

    public GuiClickType getType()
    {
        return type;
    }

    @NotNull
    public ClickType getClick()
    {
        return clickType;
    }

    public boolean isOutside()
    {
        return outside;
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
