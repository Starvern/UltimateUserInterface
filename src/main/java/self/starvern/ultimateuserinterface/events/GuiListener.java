package self.starvern.ultimateuserinterface.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
<<<<<<< Updated upstream
=======
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
>>>>>>> Stashed changes
import org.bukkit.inventory.ItemStack;

import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiCloseEvent;
import self.starvern.ultimateuserinterface.api.GuiItemClickEvent;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.managers.GuiManager;
<<<<<<< Updated upstream
=======
import self.starvern.ultimateuserinterface.managers.InventoryManager;
import self.starvern.ultimateuserinterface.utils.InventoryUtility;

import java.util.Optional;
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
=======
        UUI.getSingleton().getLogger().info(String.valueOf(event.getSlot()));

        Optional<GuiPage> pageOptional = GuiManager.getGuiPage(inventory);
        if (pageOptional.isEmpty()) return;
>>>>>>> Stashed changes

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        GuiItem guiItem = page.getItem(item);
        if (guiItem == null) return;

        Bukkit.getPluginManager().callEvent(new GuiItemClickEvent(guiItem, event.getWhoClicked(), event.getClick()));
    }

    @EventHandler
    public void GuiItemClickEvent(GuiItemClickEvent event)
    {
<<<<<<< Updated upstream
=======
        event.getPage().runClickEvent(event);
>>>>>>> Stashed changes
        event.getItem().runEvent(event);
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent event)
    {
        Inventory inventory = event.getInventory();

        Optional<GuiPage> pageOptional = GuiManager.getGuiPage(inventory);
        if (pageOptional.isEmpty()) return;

        Bukkit.getPluginManager().callEvent(new GuiCloseEvent(pageOptional.get(), event.getPlayer()));
    }

    @EventHandler
    public void GuiCloseEvent(GuiCloseEvent event)
    {
        InventoryManager.restoreInventory(event.getWhoClicked().getUniqueId());
        event.getPage().runCloseEvent(event);
    }
}
