package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.lib.GuiPage;

/**
 * Runs every x ticks as specified by GuiPage#getTick
 * @since 0.4.2
 */
public class GuiTickEvent extends GuiEvent
{
    private static final HandlerList handlers = new HandlerList();

    public GuiTickEvent(@NotNull HumanEntity human, @NotNull GuiPage page)
    {
        super(human, page);
    }

    @NotNull
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
