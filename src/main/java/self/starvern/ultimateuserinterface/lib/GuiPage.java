package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.api.GuiItemClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class GuiPage
{
    private final UUI api;

    private final Gui gui;
    private String title;
    private final List<String> pattern;
    private final List<GuiItem> items;
    private Inventory inventory;
    private Consumer<GuiItemClickEvent> globalEvent;

    public GuiPage(UUI api, Gui gui, List<String> pattern)
    {
        this.api = api;
        this.gui = gui;
        this.title = this.gui.getTitle();
        this.pattern = pattern;
        this.items = new ArrayList<>();
        this.inventory = Bukkit.createInventory(null, Math.min(9 * this.pattern.size(), 54), this.title);
    }

    /**
     * @return The GUI this page is inside.
     * @since 0.1.0
     */
    public Gui getGui()
    {
        return this.gui;
    }

    /**
     * Set an event to run anytime an item from this page is clicked.
     * @param globalEvent The event to run
     * @return The instance of GuiPage
     * @since 0.2.3
     */
    public GuiPage setGlobalEvent(Consumer<GuiItemClickEvent> globalEvent)
    {
        this.globalEvent = globalEvent;
        return this;
    }

    /**
     * Executes the global event for this page.
     * @param event The event to run.
     * @return The instance of GuiPage.
     */
    public GuiPage runEvent(@NotNull GuiItemClickEvent event)
    {
        if (this.globalEvent == null) return this;
        this.globalEvent.accept(event);
        return this;
    }

    /**
     * Update's the inventory with current items.
     * @return An instance of GuiPage
     * @since 0.2.3
     */
    public GuiPage update()
    {
        this.inventory = Bukkit.createInventory(null, 9 * this.pattern.size(), this.title);
        return this;
    }

    /**
     * Update's the inventory with its original items.
     * @return An instance of GuiPage
     * @since 0.2.3
     */
    public GuiPage refresh()
    {
        return this.update().loadItems();
    }

    /**
     * @return The page's title. Defaults to the GUI's title.
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * @param title The new title of the page.
     * @since 0.2.3
     */
    public GuiPage setTitle(String title)
    {
        this.title = title;
        return this;
    }

    /**
     * Creates a clean duplicate of the page.
     * @return The new instance of GuiPage.
     * @since 0.1.5
     */
    public GuiPage duplicate()
    {
        return new GuiPage(this.api, this.gui, this.pattern).loadItems();
    }

    /**
     * Constructs a list of GuiItems based on the pattern
     * @since 0.1.0
     */
    public GuiPage loadItems()
    {
        this.items.clear();
        int slot = 0;
        for (String line : this.pattern)
        {
            for (char character : line.toCharArray())
            {
                String letter = String.valueOf(character);
                GuiItem item = new GuiItem(this.api, this, letter, slot++);
                this.items.add(item);
            }
        }
        return this;
    }

    /**
     * @return The inventory constructed from the pattern.
     * @since 0.1.0
     */
    public Inventory getInventory()
    {
        for (GuiItem item : this.items)
        {
            this.inventory.setItem(item.getSlot(), item.getItem().build());
        }

        return this.inventory;
    }

    /**
     * @return A list of all items in this page.
     * @since 0.1.6
     */
    public List<GuiItem> getItems()
    {
        return this.items;
    }

    /**
     * @param item The item to compare to the GuiItem.
     * @return The GuiItem, or if the item doesn't match anything, null.
     * @since 0.1.0
     */
    public Optional<GuiItem> getItem(ItemStack item)
    {
        if (item == null)
            return Optional.empty();

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return Optional.empty();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (!container.has(new NamespacedKey(api.getPlugin(), "uui-item-id"), PersistentDataType.STRING))
            return Optional.empty();

        String rawUUID = container.get(new NamespacedKey(api.getPlugin(), "uui-item-id"), PersistentDataType.STRING);
        UUID uuid = UUID.fromString(rawUUID);

        for (GuiItem guiItem : this.items)
        {
            if (guiItem.getUniqueId().equals(uuid))
                return Optional.of(guiItem);
        }
        return Optional.empty();
    }

    /**
     * @param id The character assigned to the items.
     * @return All the items with the assigned to the designated character.
     * @since 0.1.7
     */
    public List<GuiItem> getItems(String id)
    {
        List<GuiItem> items = new ArrayList<>();

        for (GuiItem item : this.items)
        {
            if (item.getId().equalsIgnoreCase(id))
                items.add(item);
        }

        return items;
    }

    /**
     * @return True if the page is the first of the GUI.
     * @since 0.1.7
     */
    public boolean isFirst()
    {
        return this.gui.indexOf(this) == 0;
    }

    /**
     * @return True if the page is the last of the GUI.
     * @since 0.1.7
     */
    public boolean isLast()
    {
        return this.gui.indexOf(this) == this.gui.getPages().size()-1;
    }

    /**
     * @return The next page of the GUI, or the first page if it's last.
     * @since 0.1.7
     */
    public GuiPage next()
    {
        try
        {
            return this.gui.getPage(this.gui.indexOf(this)+1);
        }
        catch (IndexOutOfBoundsException exception)
        {
            return this.gui.getPage(0);
        }
    }

    /**
     * @return The previous page of the GUI, or the last page if it's first.
     * @since 0.1.7
     */
    public GuiPage last()
    {
        try
        {
            return this.gui.getPage(this.gui.indexOf(this)-1);
        }
        catch (IndexOutOfBoundsException exception)
        {
            return this.gui.getPage(this.gui.getPages().size()-1);
        }
    }

    /**
     * Open the GUI page for an entity.
     * @param entity The entity to open the GUI page for.
     * @since 0.1.7
     */
    public void open(HumanEntity entity)
    {
        entity.openInventory(this.getInventory());
    }

    /**
     * Open the GUI page for a player.
     * @param player The player to open the GUI page for.
     * @since 0.1.7
     */
    public void open(Player player)
    {
        player.openInventory(this.getInventory());
    }

    /**
     * Gets the item at the specified slot.
     * @param slot The slot.
     * @return The instance of GuiItem, or null.
     * @since 0.2.3
     */
    public Optional<GuiItem> getItemAt(int slot)
    {
        for (GuiItem item : this.items)
        {
            if (item.getSlot() == slot) return Optional.of(item);
        }

        return Optional.empty();
    }
}
