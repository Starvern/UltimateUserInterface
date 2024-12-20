package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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

import javax.swing.text.html.Option;
import java.io.File;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * <p>
 *     Represents an inventory, which has items that could be
 *     GuiItems, and has events strapped to it.
 * </p>
 * @since 0.4.2
 */
public class GuiPage extends Actionable<GuiPage> implements InventoryHolder, GuiBased
{
    private final UUI api;
    private final Gui gui;

    private final List<String> pattern;
    private final Inventory inventory;
    private final ConfigurationSection config;

    private final List<GuiItem> items;
    private final List<SlottedGuiItem> slottedItems;

    private String title;
    private int tick;

    protected GuiPage(UUI api, Gui gui, List<String> pattern)
    {
        super();
        this.api = api;
        this.gui = gui;
        this.title = this.gui.getTitle();
        this.pattern = pattern;
        this.items = new ArrayList<>();
        this.slottedItems = new ArrayList<>();
        this.inventory = Bukkit.createInventory(
                this,
                Math.min(9 * this.pattern.size(), 54),
                ChatManager.colorize(this.title));
        this.tick = this.gui.getConfig().getInt("tick", 1000);
        this.config = gui.getConfig();
    }

    /**
     * @return A copy of the original GuiPage.
     * @since 0.4.2
     */
    public GuiPage duplicate()
    {
        GuiPage page = new GuiPage(this.api, this.gui, this.pattern);
        page.loadItems();
        page.loadActions();
        return page;
    }

    /**
     * Creates and loads GuiItems based on the pattern.
     * @return The instance of GuiPage.
     * @since 0.4.0
     */
    public GuiPage loadItems()
    {
        this.items.clear();

        Set<String> allKeys = this.getConfig().getKeys(false);
        Map<String, GuiItem> keyItems = new HashMap<>();

        // Check for all items
        for (String key : allKeys) {
            ConfigurationSection section = this.getConfig().getConfigurationSection(key);
            if (section == null)
                continue;

            if (section.getString("material") != null)
            {
                GuiItem item = new GuiItem(api, this, key);
                item.loadActions();
                this.items.add(item);
                keyItems.put(item.getId(), item);
            }
        }

        int slot = -1;
        for (String line : pattern)
        {
            for (char character : line.toCharArray())
            {
                String letter = String.valueOf(character);
                GuiItem item = keyItems.get(letter);
                slot++;

                if (item == null)
                    continue;

                SlottedGuiItem slottedItem = new SlottedGuiItem(this.api, item, slot);
                slottedItem.loadActions();
                this.setItem(slottedItem);
            }
        }

        return this;
    }

    /**
     * Loads all macros for this page, as defined in the file.
     * @since 0.4.0
     */
    @Override
    public void loadActions()
    {
        this.clearActions();

        ConfigurationSection actionList = this.config.getConfigurationSection("actions");

        if (actionList == null)
            return;

        for (ActionType type : ActionType.values())
        {
            for (String action : actionList.getStringList(type.toString()))
            {
                Optional<Macro> optionalMacro = this.api.getMacroManager().getMacro(action);
                if (optionalMacro.isEmpty()) continue;
                this.addAction(new GuiAction<>(this, optionalMacro.get(), type, action));
            }
        }
    }

    /**
     * Updates the inventory with the current SlottedGuiItems.
     * @since 0.4.2
     */
    public void update() {
        for (SlottedGuiItem item : this.slottedItems)
            inventory.setItem(item.getSlot(), item.getItem());
    }

    /**
     * Executes all macros for the page.
     * @param event The event to run the macros for.
     * @since 0.4.0
     */
    public void execute(GuiEvent event)
    {
        List<GuiAction<GuiPage>> actions = new ArrayList<>(this.actions);
        for (GuiAction<GuiPage> action : actions)
            action.execute(event);
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

        this.addAction(new GuiAction<>(this, macro, ActionType.CLICK, ""));

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
    public Optional<SlottedGuiItem> getItemAt(int slot)
    {
        return this.slottedItems.stream()
                .filter(item -> item.getSlot() == slot)
                .findFirst();
    }

    /**
     * @return All items registered in the GuiPage.
     * @since 0.4.0
     */
    public List<GuiItem> getItems()
    {
        return items;
    }

    /**
     * @return All items placed within the GuiPage.
     * @since 0.4.2
     */
    public List<SlottedGuiItem> getSlottedItems()
    {
        return slottedItems;
    }

    /**
     * @return The item registered in the GuiPage.
     * @param character The ID of the item.
     * @since 0.4.0
     */
    public Optional<GuiItem> getItem(String character)
    {
        return items.stream()
                .filter(item -> item.getId().equalsIgnoreCase(character))
                .findFirst();
    }

    /**
     * @return All items found in the GuiPage.
     * @since 0.4.0
     */
    public List<SlottedGuiItem> getSlottedItems(String character)
    {
        return slottedItems.stream()
                .filter(item -> item.getId().equalsIgnoreCase(character))
                .collect(Collectors.toList());
    }

    /**
     * Loads the original version of all GuiItems.
     * @since 0.4.0
     */
    public void reloadItems()
    {
        for (GuiItem item : this.items)
        {
            item.restoreItem();
            item.setItem(item.getItem());
        }
        for (SlottedGuiItem item : this.slottedItems)
        {
            item.restoreItem();
            item.setItem(item.getItem());
        }
    }

    /**
     * <p>
     *      Sets a GuiItem in the inventory.
     *      If a SlottedGuiItem is already at that location, it will be overridden.
     * </p>
     * @param item The item to set.
     * @return The instance of GuiPage
     * @since 0.4.2
     */
    public GuiPage setItem(SlottedGuiItem item)
    {
        List<Integer> slots = this.slottedItems.stream().map(SlottedGuiItem::getSlot).toList();

        if (slots.contains(item.getSlot()))
        {
            Optional<SlottedGuiItem> possibleItem = slottedItems.stream()
                    .filter(i -> i.getSlot() == item.getSlot())
                    .findFirst();

            possibleItem.ifPresent(slottedItems::remove);
        }

        this.slottedItems.add(item);

        this.inventory.setItem(item.getSlot(), item.getItem());
        this.update();
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
    public void open(HumanEntity entity)
    {
        // Open inventory first.
        entity.openInventory(this.getInventory());
        GuiSession.start(this.api, this, entity);
    }

    /**
     * Open the GUI page for an entity.
     * @param entity The entity to open the GUI page for.
     * @param newSession Whether to start a new GuiSession
     * @since 0.4.2
     */
    public void open(HumanEntity entity, boolean newSession)
    {
        Set<GuiSession> sessions = this.getGui().getSessions();
        List<UUID> viewerUUIDs = sessions.stream()
                .map(s -> s.getViewer().getUniqueId()).toList();

        // Open inventory first.
        entity.openInventory(this.getInventory());

        if (!newSession && viewerUUIDs.contains(entity.getUniqueId()))
            return;

        GuiSession.start(this.api, this, entity);
    }
}
