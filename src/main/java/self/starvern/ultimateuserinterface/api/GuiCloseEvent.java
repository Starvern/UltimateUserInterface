package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.lib.GuiPage;

public class GuiCloseEvent extends GuiEvent
{
    private static final HandlerList handlers = new HandlerList();

    public GuiCloseEvent(@NotNull HumanEntity human, @NotNull GuiPage page)
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
