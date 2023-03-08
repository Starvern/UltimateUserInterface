package self.starvern.ultimateuserinterface.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.api.GuiItemClickEvent;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;

import java.util.Optional;

public class GuiListener implements Listener
{
    private final UUIPlugin plugin;
    private final UUI api;

    public GuiListener(UUI api)
    {
        this.api = api;
        this.plugin = this.api.getPlugin();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event)
    {
        Inventory inventory = event.getInventory();

        HumanEntity human = event.getWhoClicked();

        if (this.api.getItemInputManager().isListeningFor(human.getUniqueId()))
            this.api.getItemInputManager().consumeInput(human.getUniqueId(), event.getCurrentItem());

        Optional<GuiPage> pageOptional = this.plugin.getApi().getGuiManager().getGuiPage(inventory);
        if (pageOptional.isEmpty()) return;

        Optional<GuiItem> guiItemOptional = pageOptional.get().getItemAt(event.getSlot());
        if (guiItemOptional.isEmpty()) return;

        GuiItemClickEvent guiClickEvent = new GuiItemClickEvent(guiItemOptional.get(), human, event.getClick());

        Bukkit.getPluginManager().callEvent(guiClickEvent);
        event.setCancelled(guiClickEvent.isCancelled());
    }

    @EventHandler
    public void InventoryDragEvent(InventoryDragEvent event)
    {
        Inventory inventory = event.getInventory();

        Optional<GuiPage> pageOptional = plugin.getApi().getGuiManager().getGuiPage(inventory);
        if (pageOptional.isEmpty()) return;

        if (event.getRawSlots().size() <= 1)
        {
            event.setCancelled(true);
            return;
        }

        int slot = event.getRawSlots().toArray(new Integer[0])[0];

        Optional<GuiItem> guiItemOptional = pageOptional.get().getItemAt(slot);
        if (guiItemOptional.isEmpty()) return;

        GuiItemClickEvent guiClickEvent = new GuiItemClickEvent(
                guiItemOptional.get(),
                event.getWhoClicked(),
                (event.getType().equals(DragType.EVEN)) ? ClickType.LEFT : ClickType.RIGHT);

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
