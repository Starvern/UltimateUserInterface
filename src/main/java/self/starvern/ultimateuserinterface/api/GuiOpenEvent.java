package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.lib.GuiPage;

public class GuiOpenEvent extends GuiEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;

    public GuiOpenEvent(@NotNull HumanEntity human, @NotNull GuiPage page)
    {
        super(human, page);
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
