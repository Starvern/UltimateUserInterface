package self.starvern.ultimateuserinterface.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiItemClickEvent;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.managers.GuiManager;
import self.starvern.ultimateuserinterface.utils.InventoryUtility;

public class GuiListener implements Listener
{
    public GuiListener()
    {
        Bukkit.getPluginManager().registerEvents(this, UUI.getSingleton());
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event)
    {
        Inventory inventory = event.getInventory();

        GuiPage page = GuiManager.getGuiPage(inventory);
        if (page == null) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        GuiItem guiItem = page.getItemAt(InventoryUtility.getSlot(inventory, item));
        if (guiItem == null) return;

        GuiItemClickEvent guiClickEvent = new GuiItemClickEvent(guiItem, event.getWhoClicked(), event.getClick());
        Bukkit.getPluginManager().callEvent(guiClickEvent);
        event.setCancelled(guiClickEvent.isCancelled());
    }

    @EventHandler
    public void GuiItemClickEvent(GuiItemClickEvent event)
    {
        event.getPage().runEvent(event);
        event.getItem().runEvent(event);
    }
}
