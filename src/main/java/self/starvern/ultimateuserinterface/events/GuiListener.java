package self.starvern.ultimateuserinterface.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.api.GuiItemClickEvent;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.utils.InventoryUtility;

import java.util.Optional;

public class GuiListener implements Listener
{
    private final UUIPlugin plugin;

    public GuiListener(UUIPlugin plugin)
    {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event)
    {
        Inventory inventory = event.getInventory();

        Optional<GuiPage> pageOptional = plugin.getApi().getGuiManager().getGuiPage(inventory);
        if (pageOptional.isEmpty()) return;

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        Optional<GuiItem> guiItemOptional = pageOptional.get().getItemAt(InventoryUtility.getSlot(inventory, item));
        if (guiItemOptional.isEmpty()) return;

        GuiItemClickEvent guiClickEvent = new GuiItemClickEvent(
                guiItemOptional.get(), event.getWhoClicked(), event.getClick());
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
