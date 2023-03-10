package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiPage;

/**
 * Runs every tick
 */
public class GuiTickEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private final Gui gui;
    private final GuiPage page;
    private final HumanEntity human;

    public GuiTickEvent(@NotNull HumanEntity human, @NotNull GuiPage page)
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

    @NotNull
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
