package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;

import java.util.Optional;

/**
 * <p>
 *     Called when:
 *     1) A GuiItem is directly clicked.
 *     2) An item is shift clicked into a GuiItem's slot (only if the GuiItem is AIR)
 *     3) An item is stacked in a double click item collection
 * </p>
 */
public class GuiClickEvent extends GuiEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private final GuiItem item;
    private final ClickType clickType;
    private final boolean outside;

    private boolean cancel;

    public GuiClickEvent(@NotNull HumanEntity human, @NotNull GuiPage page, @NotNull ClickType clickType,
                         @Nullable GuiItem item, boolean outside)
    {
        super(human, page);
        this.item = item;
        this.clickType = clickType;
        this.outside = outside;
    }

    public Optional<GuiItem> getItem()
    {
        if (item == null)
            return Optional.empty();
        return Optional.of(item);
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
