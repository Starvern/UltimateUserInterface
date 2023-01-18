package self.starvern.ultimateuserinterface.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiItem;
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
        Gui gui = GuiManager.getGui(event.getView().getTopInventory());
        if (gui == null) return;

        event.setCancelled(true);
        event.setResult(Event.Result.DENY);

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        GuiItem guiItem = gui.getItem(item);
        if (guiItem == null) return;

        guiItem.runEvent(event);
    }
}
