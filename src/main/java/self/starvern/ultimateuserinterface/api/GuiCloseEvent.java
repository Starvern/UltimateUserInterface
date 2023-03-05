package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import org.jetbrains.annotations.NotNull;

import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiPage;

public class GuiCloseEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private final Gui gui;
    private final GuiPage page;
    private final HumanEntity human;

    public GuiCloseEvent(@NotNull GuiPage page, @NotNull HumanEntity human)
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

    @NotNull
    public HumanEntity getWhoClicked()
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
