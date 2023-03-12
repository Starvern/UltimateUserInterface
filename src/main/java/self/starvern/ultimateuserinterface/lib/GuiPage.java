package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiClickEvent;
import self.starvern.ultimateuserinterface.api.GuiDragEvent;
import self.starvern.ultimateuserinterface.api.GuiOpenEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * <p>
 *     Represents an inventory, which has items that could be
 *     GuiItems, and has events strapped to it.
 * </p>
 */
public class GuiPage implements InventoryHolder, GuiBased
{
    private final UUI api;

    private final Gui gui;
    private String title;
    private final List<String> pattern;
    private final List<GuiItem> items;
    private final Inventory inventory;
    private final int tick;

    private GuiPage(UUI api, Gui gui, List<String> pattern)
    {
        this.api = api;
        this.gui = gui;
        this.title = this.gui.getTitle();
        this.pattern = pattern;
        this.items = new ArrayList<>();
        this.inventory = Bukkit.createInventory(this, Math.min(9 * this.pattern.size(), 54), this.title);
        this.tick = this.gui.getConfig().getInt("tick", 20);
    }

    protected static GuiPage createPage(UUI api, Gui gui, List<String> pattern)
    {
        GuiPage guiPage = new GuiPage(api, gui, pattern);

        int slot = 0;
        for (String line : pattern)
        {
            for (char character : line.toCharArray())
            {
                String letter = String.valueOf(character);
                GuiItem item = new GuiItem(api, guiPage, letter, slot++);
                guiPage.setItem(item);
            }
        }

        return guiPage;
    }

    /**
     * @return How often to fire the GuiTickEvent.
     */
    public int getTick()
    {
        return tick;
    }

    public Optional<GuiItem> getItemAt(int slot)
    {
        return this.items.stream()
                .filter(item -> item.getSlot() == slot)
                .findFirst();
    }

    public List<GuiItem> getItems()
    {
        return items;
    }

    public GuiPage reloadItems()
    {
        for (GuiItem item : this.items)
        {
            item.reloadItem();
            item.setItem(item.getItem());
        }
        return this;
    }

    /**
     * Sets a GuiItem in the inventory.
     * @param item The item to set.
     * @return The instance of GuiPage
     * @since 0.4.0
     */
    public GuiPage setItem(GuiItem item)
    {
        if (!this.items.contains(item))
            this.items.add(item);
        this.inventory.setItem(item.getSlot(), item.getItem());
        return this;
    }

    /**
     * <p>
     *     Sets the item, either replacing an empty slot,
     *     or setting the ItemStack of a GuiItem.
     * </p>
     * @param item The item to set.
     * @param slot Where to put the item.
     * @return The instance of GuiPage
     * @since 0.4.0
     */
    public GuiPage setItem(ItemStack item, int slot)
    {
        for (GuiItem guiItem : this.items)
        {
            if (guiItem.getSlot() == slot)
            {
                guiItem.setItem(item);
                return this;
            }
        }

        this.inventory.setItem(slot, item);
        return this;
    }

    /**
     * @return The GUI this page is inside.
     * @since 0.1.0
     */
    @Override
    public Gui getGui()
    {
        return this.gui;
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
     * @return The inventory constructed from the pattern.
     * @since 0.1.0
     */
    @Override
    public @NotNull Inventory getInventory()
    {
        return this.inventory;
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
     * @since 0.4.0
     */
    public GuiSession open(HumanEntity entity)
    {
        return GuiSession.start(this.api, this, entity);
    }
}
