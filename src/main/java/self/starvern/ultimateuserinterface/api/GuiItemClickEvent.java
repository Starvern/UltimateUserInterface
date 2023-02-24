package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;

public class GuiItemClickEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private final Gui gui;
    private final GuiPage page;
    private final GuiItem item;
    private final HumanEntity human;
    private final ClickType clickType;

    private boolean cancel;

    public GuiItemClickEvent(@NotNull GuiItem item, @NotNull HumanEntity human, @NotNull ClickType clickType)
    {
        this.gui = item.getPage().getGui();
        this.page = item.getPage();
        this.item = item;
        this.human = human;
        this.clickType = clickType;
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
    public GuiItem getItem()
    {
        return item;
    }

    @NotNull
    public HumanEntity getWhoClicked()
    {
        return human;
    }

    @NotNull
    public ClickType getClick()
    {
        return clickType;
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
