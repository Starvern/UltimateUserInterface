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
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.api.*;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.GuiSession;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
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
        Inventory inventory = view.getTopInventory();
        HumanEntity human = event.getWhoClicked();

        if (this.api.getItemInputManager().isListeningFor(human.getUniqueId()))
            this.api.getItemInputManager().consumeInput(human.getUniqueId(), event.getCurrentItem());

        boolean outside = event.getSlotType().equals(InventoryType.SlotType.OUTSIDE);

        @Nullable GuiPage page = this.plugin.getApi().getGuiManager().getGuiPage(inventory);
        if (page == null)
            return;

        // At this point, we are inside a view containing a GuiPage

        // This variable checks if our click event happens inside that GuiPage
        boolean insidePage = this.api.getGuiManager().getGuiPage(view.getInventory(event.getRawSlot())) != null;

        InventoryAction action = event.getAction();

        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        ClickType clickType = event.getClick();

        // When the user moves an item to the other inventory,
        // We will use the insidePage variable to determine if you're
        // taking or putting items.
        if (action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) & inventory.firstEmpty() != -1)
        {
            @Nullable SlottedGuiItem guiItem = page.getItemAt(inventory.firstEmpty());

            // Later we will set the GuiItem to event.getCurrentItem

            // Because the item that is clicked is the one being shift-clicked
            GuiClickEvent clickEvent = new GuiClickEvent(
                    human,
                    page,
                    clickType,
                    guiItem,
                    currentItem,
                    cursor,
                    action,
                    (insidePage) ? GuiClickType.TAKE : GuiClickType.PUT,
                    outside
            );
            Bukkit.getPluginManager().callEvent(clickEvent);
            event.setCancelled(clickEvent.isCancelled());
        }

        if (event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR) && inventory.containsAtLeast(cursor, 1))
        {
            ItemStack[] contents = inventory.getStorageContents();

            for (int itemIndex = 0; itemIndex < contents.length; itemIndex++)
            {
                ItemStack item = contents[itemIndex];
                if (item == null) continue;

                if (!item.isSimilar(cursor))
                    continue;

                @Nullable SlottedGuiItem guiItem = page.getItemAt(itemIndex);

                /*
                    Since you are recalling the items to the cursor, you are
                    always taking from the GuiPage.
                 */

                GuiClickEvent clickEvent = new GuiClickEvent(
                        human,
                        page,
                        clickType,
                        guiItem,
                        item,
                        cursor,
                        action,
                        GuiClickType.TAKE,
                        outside
                );

                Bukkit.getPluginManager().callEvent(clickEvent);
                event.setCancelled(clickEvent.isCancelled());
            }
        }

        if (insidePage && !outside)
        {
            @Nullable SlottedGuiItem guiItem = page.getItemAt(event.getSlot());

            /*
             * When clicking an item, we must check if the cursor
             * is air to determine if the item is being taken.
             */

            boolean cursorIsAir = cursor.getType().isAir();

            GuiClickType guiClickType = GuiClickType.PUT;

            if (!cursorIsAir && currentItem != null && !currentItem.isSimilar(cursor))
                guiClickType = GuiClickType.SWAP;

            if (cursorIsAir && currentItem != null)
                guiClickType = GuiClickType.TAKE;

            GuiClickEvent clickEvent = new GuiClickEvent(
                    human,
                    page,
                    clickType,
                    guiItem,
                    currentItem,
                    cursor,
                    action,
                    guiClickType,
                    false
            );

            Bukkit.getPluginManager().callEvent(clickEvent);
            event.setCancelled(clickEvent.isCancelled());
        }
    }

    @EventHandler
    public void InventoryDragEvent(InventoryDragEvent event)
    {
        InventoryView view = event.getView();

        GuiPage page = null;

        for (int slot : event.getRawSlots())
        {
           page = plugin.getApi().getGuiManager().getGuiPage(view.getInventory(slot));
        }

        if (page == null) return;

        if (event.getRawSlots().size() <= 1)
        {
            int slot = event.getRawSlots().toArray(new Integer[0])[0];

            @Nullable SlottedGuiItem guiItem = page.getItemAt(slot);

            GuiClickEvent guiClickEvent = new GuiClickEvent(
                    event.getWhoClicked(),
                    page,
                    (event.getType().equals(DragType.EVEN)) ? ClickType.LEFT : ClickType.RIGHT,
                    guiItem,
                    page.getInventory().getItem(slot),
                    event.getCursor(),
                    (event.getType().equals(DragType.EVEN)) ? InventoryAction.PLACE_SOME : InventoryAction.PLACE_ONE,
                    GuiClickType.PUT,
                    false
            );

            Bukkit.getPluginManager().callEvent(guiClickEvent);
            event.setCancelled(guiClickEvent.isCancelled());
            return;
        }

        List<SlottedGuiItem> items = new ArrayList<>();
        Map<Integer, ItemStack> newItems = event.getNewItems();

        for (int slot : event.getRawSlots())
        {
            @Nullable SlottedGuiItem possibleItem = page.getItemAt(slot);
            if (possibleItem == null)
                continue;
            items.add(possibleItem);
        }

        GuiDragEvent guiDragEvent = new GuiDragEvent(
                event.getWhoClicked(),
                page,
                event.getType(),
                items,
                newItems,
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

        @Nullable GuiPage page = this.api.getGuiManager().getGuiPage(inventory);
        if (page == null)
            return;

        GuiOpenEvent guiOpenEvent = new GuiOpenEvent(
                human,
                page
        );

        Bukkit.getPluginManager().callEvent(guiOpenEvent);
        event.setCancelled(guiOpenEvent.isCancelled());
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent event)
    {
        Inventory inventory = event.getInventory();
        HumanEntity human = event.getPlayer();

        @Nullable GuiPage page = this.api.getGuiManager().getGuiPage(inventory);
        if (page == null)
            return;

        GuiCloseEvent guiCloseEvent = new GuiCloseEvent(
                human,
                page
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
        if (event.getItem().isEmpty())
            return;

        GuiItem item = event.getItem().get();
        item.execute(event);
    }

    @EventHandler
    public void GuiDragEvent(GuiDragEvent event)
    {
        event.getPage().execute(event);
        for (SlottedGuiItem item : event.getItems())
            item.execute(event);
    }

    @EventHandler
    public void GuiOpenEvent(GuiOpenEvent event)
    {
        GuiSession session = null;

        for (GuiSession activeSession : event.getGui().getSessions())
        {
            if (activeSession.getViewer().getUniqueId().equals(event.getHuman().getUniqueId()))
            {
                session = activeSession;
                break;
            }
        }

        GuiPage page = event.getPage();
        page.execute(event);

        for (SlottedGuiItem item : new ArrayList<>(page.getSlottedItems()))
            item.execute(event);

        if (event.isCancelled())
        {
            if (session != null)
                session.endSession();
        }
    }

    @EventHandler
    public void GuiCloseEvent(GuiCloseEvent event)
    {
        GuiPage page = event.getPage();
        //this.enforceItems(page);

        page.execute(event);
        ItemUtility.removeUUID(this.api, event.getHuman().getItemOnCursor());

        for (ItemStack item : event.getHuman().getInventory().getContents())
        {
            if (item == null) continue;
            ItemUtility.removeUUID(this.api, item);
        }

        for (GuiItem item : page.getItems())
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
        GuiPage page = event.getPage();

        // Assigned to prevent ConcurrentModificationException
        List<SlottedGuiItem> items = new ArrayList<>(page.getSlottedItems());

        for (SlottedGuiItem item : items)
            item.execute(event);
        page.execute(event);
    }

    @EventHandler
    public void GuiCustomEvent(GuiCustomEvent event)
    {
        if (event.getType().equals(GuiCustomEvent.Type.PAGE))
        {
            event.getPage().execute(event);

            for (GuiItem item : new ArrayList<>(event.getPage().getSlottedItems()))
                item.execute(event);
            return;
        }

        if (event.getItem() != null)
            event.getItem().execute(event);
    }
}
