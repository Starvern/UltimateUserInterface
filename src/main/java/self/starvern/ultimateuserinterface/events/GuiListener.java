package self.starvern.ultimateuserinterface.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.api.*;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.GuiSession;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.*;

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
    public void InventoryCloseEvent(InventoryCloseEvent event)
    {
        Inventory inventory = event.getInventory();
        HumanEntity human = event.getPlayer();

        Optional<GuiPage> pageOptional = this.api.getGuiManager().getGuiPage(inventory);
        if (pageOptional.isEmpty())
            return;

        GuiCloseEvent guiCloseEvent = new GuiCloseEvent(
                human,
                pageOptional.get()
        );

        Bukkit.getPluginManager().callEvent(guiCloseEvent);
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        InventoryView view = player.getOpenInventory();

        InventoryHolder holder = view.getTopInventory().getHolder();

        if (!(holder instanceof GuiPage))
            return;

        ItemUtility.removeUUID(this.api, event.getItemDrop().getItemStack());
    }

    @EventHandler
    public void GuiClickEvent(GuiClickEvent event)
    {
        event.getPage().execute(event);
        event.getItem().ifPresent(item -> item.execute(event));
    }

    @EventHandler
    public void GuiDragEvent(GuiDragEvent event)
    {
        event.getPage().execute(event);
        for (GuiItem item : event.getItems())
            item.execute(event);
    }

    @EventHandler
    public void GuiOpenEvent(GuiOpenEvent event)
    {
        event.getPage().execute(event);
        for (GuiItem item : event.getPage().getItems())
            item.execute(event);

        if (event.isCancelled())
        {
            for (GuiSession session : event.getGui().getSessions())
            {
                if (session.getViewer().getUniqueId().equals(event.getHuman().getUniqueId()))
                    session.endSession();
            }
        }
    }

    @EventHandler
    public void GuiCloseEvent(GuiCloseEvent event)
    {
        event.getPage().execute(event);
        ItemUtility.removeUUID(this.api, event.getHuman().getItemOnCursor());

        for (ItemStack item : event.getHuman().getInventory().getContents())
        {
            if (item == null) continue;
            ItemUtility.removeUUID(this.api, item);
        }

        for (GuiItem item : event.getPage().getItems())
            item.execute(event);

        for (GuiSession session : event.getGui().getSessions())
        {
            if (session.getViewer().getUniqueId().equals(event.getHuman().getUniqueId()))
                session.endSession();
        }
    }

    @EventHandler
    public void GuiTickEvent(GuiTickEvent event)
    {
        event.getPage().execute(event);
        // Assigned to prevent ConcurrentModificationException
        List<GuiItem> items = new ArrayList<>();
        items.addAll(event.getPage().getItems());
        for (GuiItem item : items)
            item.execute(event);
    }
}
