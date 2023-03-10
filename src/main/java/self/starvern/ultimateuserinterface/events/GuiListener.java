package self.starvern.ultimateuserinterface.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.api.GuiClickEvent;
import self.starvern.ultimateuserinterface.api.GuiDragEvent;
import self.starvern.ultimateuserinterface.api.GuiOpenEvent;
import self.starvern.ultimateuserinterface.api.GuiTickEvent;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
        InventoryView view = event.getView();
        boolean outside = event.getSlotType().equals(InventoryType.SlotType.OUTSIDE);

        Inventory inventory;

        if (outside)
            inventory = view.getTopInventory();
        else
            inventory = view.getInventory(event.getRawSlot());

        HumanEntity human = event.getWhoClicked();

        if (this.api.getItemInputManager().isListeningFor(human.getUniqueId()))
            this.api.getItemInputManager().consumeInput(human.getUniqueId(), event.getCurrentItem());

        Optional<GuiPage> pageOptional = this.plugin.getApi().getGuiManager().getGuiPage(inventory);
        if (pageOptional.isEmpty())
        {
            if (event.isShiftClick())
            {
                Optional<GuiPage> newPageOptional = this.plugin.getApi().getGuiManager()
                        .getGuiPage(view.getTopInventory());

                if (newPageOptional.isEmpty())
                    return;

                if (inventory == null || inventory.firstEmpty() == -1)
                    return;

                Optional<GuiItem> guiItemOptional = newPageOptional.get().getItemAt(inventory.firstEmpty());
                if (guiItemOptional.isEmpty()) return;

                GuiClickEvent guiClickEvent = new GuiClickEvent(
                        human,
                        newPageOptional.get(),
                        event.getClick(),
                        guiItemOptional.orElse(null),
                        outside
                );

                Bukkit.getPluginManager().callEvent(guiClickEvent);
                event.setCancelled(guiClickEvent.isCancelled());
            }

            return;
        }

        Optional<GuiItem> guiItemOptional = pageOptional.get().getItemAt(event.getSlot());
        if (guiItemOptional.isEmpty() && !outside) return;

        GuiClickEvent guiClickEvent = new GuiClickEvent(
                human,
                pageOptional.get(),
                event.getClick(),
                guiItemOptional.orElse(null),
                outside
        );

        Bukkit.getPluginManager().callEvent(guiClickEvent);
        event.setCancelled(guiClickEvent.isCancelled());
    }

    @EventHandler
    public void InventoryDragEvent(InventoryDragEvent event)
    {
        InventoryView view = event.getView();

        GuiPage page = null;

        for (int slot : event.getRawSlots())
        {
            Optional<GuiPage> pageOptional = plugin.getApi().getGuiManager().getGuiPage(view.getInventory(slot));
            if (pageOptional.isPresent())
                page = pageOptional.get();
        }

        if (page == null) return;

        if (event.getRawSlots().size() <= 1)
        {
            int slot = event.getRawSlots().toArray(new Integer[0])[0];

            Optional<GuiItem> guiItemOptional = page.getItemAt(slot);

            GuiClickEvent guiClickEvent = new GuiClickEvent(
                    event.getWhoClicked(),
                    page,
                    (event.getType().equals(DragType.EVEN)) ? ClickType.LEFT : ClickType.RIGHT,
                    guiItemOptional.orElse(null),
                    false);

            Bukkit.getPluginManager().callEvent(guiClickEvent);
            event.setCancelled(guiClickEvent.isCancelled());
            return;
        }

        Set<GuiItem> items = new HashSet<>();

        for (int slot : event.getRawSlots())
        {
            Optional<GuiItem> possibleItem = page.getItemAt(slot);
            possibleItem.ifPresent(items::add);
        }

        GuiDragEvent guiDragEvent = new GuiDragEvent(
                event.getWhoClicked(),
                page,
                event.getType(),
                items,
                event.getOldCursor(),
                event.getCursor()
        );

        Bukkit.getPluginManager().callEvent(guiDragEvent);
        event.setCancelled(guiDragEvent.isCancelled());
    }

    @EventHandler
    public void InventoryOpenEvent(InventoryOpenEvent event)
    {
        Inventory inventory = event.getInventory();
        HumanEntity human = event.getPlayer();

        Optional<GuiPage> pageOptional = this.api.getGuiManager().getGuiPage(inventory);
        if (pageOptional.isEmpty())
            return;

        GuiOpenEvent guiOpenEvent = new GuiOpenEvent(
                human,
                pageOptional.get()
        );

        Bukkit.getPluginManager().callEvent(guiOpenEvent);
        event.setCancelled(guiOpenEvent.isCancelled());
    }

    @EventHandler
    public void GuiClickEvent(GuiClickEvent event)
    {
        if (event.getItem().isEmpty())
            return;

        Bukkit.getLogger().info("click type: " + event.getClick() + "outside: " + event.isOutside());
        Bukkit.getLogger().info("Changed: " + event.getPage().isChanged(event.getItem().get().getSlot()));
        event.getPage().runEvent(event);
        event.getItem().ifPresent(item -> item.runEvent(event));
    }

    @EventHandler
    public void GuiDragEvent(GuiDragEvent event)
    {
        for (GuiItem item : event.getItems())
        {
            Bukkit.getLogger().info("Changed: " + event.getPage().isChanged(item.getSlot()));
        }
    }

    @EventHandler
    public void GuiOpenEvent(GuiOpenEvent event)
    {
        Bukkit.getLogger().info("Opened " + event.getPage().getTitle());
    }

    @EventHandler
    public void GuiTickEvent(GuiTickEvent event)
    {
        Bukkit.getLogger().info("Tick");
        for (GuiPage page : event.getGui().getPages())
        {
            page.updateInventory();
            Bukkit.getLogger().info("Update");
        }
    }
}
