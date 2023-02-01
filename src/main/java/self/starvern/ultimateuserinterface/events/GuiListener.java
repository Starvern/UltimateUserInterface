package self.starvern.ultimateuserinterface.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiItemClickEvent;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.managers.GuiManager;

public class GuiListener implements Listener
{
    public GuiListener()
    {
        Bukkit.getPluginManager().registerEvents(this, UUI.getSingleton());
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event)
    {
        GuiPage page = GuiManager.getGuiPage(event.getView().getTopInventory());
        if (page == null) return;

        event.setCancelled(true);
        event.setResult(Event.Result.DENY);

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        GuiItem guiItem = page.getItem(item);
        if (guiItem == null) return;

        Bukkit.getPluginManager().callEvent(new GuiItemClickEvent(guiItem, event.getWhoClicked(), event.getClick()));
    }

    @EventHandler
    public void GuiItemClickEvent(GuiItemClickEvent event)
    {
        event.getItem().runEvent(event);
    }
}
