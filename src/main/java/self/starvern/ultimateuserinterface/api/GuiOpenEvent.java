package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiPage;

public class GuiOpenEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private final Gui gui;
    private final GuiPage page;
    private final HumanEntity human;

    private boolean cancel;

    public GuiOpenEvent(@NotNull HumanEntity human, @NotNull GuiPage page)
    {
        this.gui = page.getGui();
        this.page = page;
        this.human = human;
    }

    @NotNull
    public Gui getGui()
    {
        return gui;
    }

    @NotNull
    public GuiPage getPage()
    {
        return page;
    }

    public HumanEntity getHuman()
    {
        return human;
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
