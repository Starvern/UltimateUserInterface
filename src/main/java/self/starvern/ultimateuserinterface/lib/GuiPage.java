package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.macros.ActionType;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.managers.ChatManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

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

    private final List<String> pattern;
    private final List<GuiItem> items;
    private final Inventory inventory;

    private final List<GuiAction<GuiPage>> actions;

    private String title;
    private int tick;

    protected GuiPage(UUI api, Gui gui, List<String> pattern)
    {
        this.api = api;
        this.gui = gui;

        this.title = this.gui.getTitle();
        this.pattern = pattern;
        this.items = new ArrayList<>();
        this.inventory = Bukkit.createInventory(this, Math.min(9 * this.pattern.size(), 54),
                ChatManager.colorize(this.title));
        this.actions = new ArrayList<>();
        this.tick = this.gui.getConfig().getInt("tick", 20);
    }

    public GuiPage duplicate()
    {
        return new GuiPage(this.api, this.gui, this.pattern).loadItems().loadActions();
    }

    /**
     * Creates and loads GuiItems based on the pattern.
     * @return The instance of GuiPage.
     * @since 0.4.0
     */
    public GuiPage loadItems()
    {
        this.items.clear();

        int slot = 0;
        for (String line : pattern)
        {
            for (char character : line.toCharArray())
            {
                String letter = String.valueOf(character);
                GuiItem item = new GuiItem(api, this, letter, slot++);
                this.setItem(item.loadActions());
            }
        }

        return this;
    }

    /**
     * Loads all macros for this page, as defined in the file.
     * @return The instance of GuiPage.
     * @since 0.4.0
     */
    public GuiPage loadActions()
    {
        for (ActionType type : ActionType.values())
        {
            for (String action : this.getConfig().getStringList("actions." + type))
            {
                Optional<Macro> optionalMacro = this.api.getMacroManager().getMacro(action);
                if (optionalMacro.isEmpty()) continue;
                actions.add(new GuiAction<>(this, optionalMacro.get(), type, action));
            }
        }
        return this;
    }

    /**
     * Updates the inventory with the current GuiItems.
     * @return The instance of GuiPage.
     * @since 0.4.2
     */
    public GuiPage update()
    {
        for (GuiItem item : this.items)
        {
            inventory.setItem(item.getSlot(), item.getItem());
        }
        return this;
    }

    /**
     * Executes all macros for the page.
     * @param event The event to run the macros for.
     * @return The instance of GuiPage.
     * @since 0.4.0
     */
    public GuiPage execute(GuiEvent event)
    {
        List<GuiAction<GuiPage>> actions = new ArrayList<>(this.actions);
        for (GuiAction<GuiPage> action : actions)
            action.execute(event);

        return this;
    }

    /**
     * A shorthand for creating macros on the fly.
     * @param consumer The GuiEvent and GuiAction to provide to the Macro#run method.
     * @return The instance of GuiPage.
     * @since 0.4.2
     */
    public GuiPage execute(BiConsumer<GuiEvent, GuiAction<GuiPage>> consumer)
    {
        Macro macro = new Macro(this.api, this.api.getPlugin(), "") {
            @Override
            public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
            {
                if (action.getHolder() instanceof GuiPage)
                    consumer.accept(event, (GuiAction<GuiPage>) action);
            }
        };

        this.actions.add(new GuiAction<>(this, macro, ActionType.CLICK, ""));

        return this;
    }

    /**
     * @return The File this GuiPage is found in.
     * @since 0.4.0
     */
    public File getFile()
    {
        return this.gui.getFile();
    }

    /**
     * @return The configuration based on the GuiPage's file.
     * @since 0.4.0
     */
    public FileConfiguration getConfig()
    {
        return this.gui.getConfig();
    }

    /**
     * @return How often to fire the GuiTickEvent (in ticks).
     * @since 0.4.0
     */
    public int getTick()
    {
        return this.tick;
    }

    /**
     * @param tick The new tick for the GuiPage.
     * @return The instance of GuiPage.
     * @since 0.4.0
     */
    public GuiPage setTick(int tick)
    {
        this.tick = tick;
        return this;
    }

    /**
     * @param slot The slot to check.
     * @return An Optional which, if present, contains the GuiItem.
     */
    public Optional<GuiItem> getItemAt(int slot)
    {
        return this.items.stream()
                .filter(item -> item.getSlot() == slot)
                .findFirst();
    }

    /**
     * @param slot The slot to get from.
     * @return The ItemStack in the slot.
     */
    public ItemStack getItemStackAt(int slot)
    {
        return this.inventory.getItem(slot);
    }

    /**
     * @return All items found in the GuiPage.
     * @since 0.4.0
     */
    public List<GuiItem> getItems()
    {
        return items;
    }

    /**
     * @return All items found in the GuiPage.
     * @since 0.4.0
     */
    public List<GuiItem> getItems(String character)
    {
        return items.stream()
                .filter(item -> item.getId().equalsIgnoreCase(character))
                .collect(Collectors.toList());
    }

    /**
     * Loads the original version of all GuiItems.
     * @return The instance of GuiPage.
     * @since 0.4.0
     */
    public GuiPage reloadItems()
    {
        for (GuiItem item : this.items)
        {
            item.restoreItem();
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
     * @since 0.4.0
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
