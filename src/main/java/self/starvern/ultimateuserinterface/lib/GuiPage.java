package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.macros.ActionTrigger;
import self.starvern.ultimateuserinterface.macros.ActionType;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.managers.ChatManager;
import self.starvern.ultimateuserinterface.properties.GuiProperties;

import java.io.File;
import java.util.*;
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
    private Inventory inventory;
    private final ConfigurationSection config;

    private final List<GuiItem> items;
    private final List<SlottedGuiItem> slottedItems;

    private String title;
    private int tick;

    private final GuiProperties<GuiPage> properties;

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
        this.properties = new GuiProperties<>(this);
        this.properties.loadProperties();
        this.loadActions();
        this.loadItems();
    }

    /**
     * @return Instance of UUI api.
     * @since 0.5.0
     */
    public UUI getApi()
    {
        return this.api;
    }

    /**
     * @return Any properties this page has.
     * @since 0.5.0
     */
    public GuiProperties<GuiPage> getProperties()
    {
        return this.properties;
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
        page.getProperties().loadProperties();
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

        // Check for all items, loads any GuiItems
        for (String key : allKeys)
        {
            ConfigurationSection section = this.getConfig().getConfigurationSection(key);
            if (section == null)
                continue;

            if (section.getString("material") != null)
            {
                GuiItem item = new GuiItem(api, this, key);
                this.items.add(item);
                keyItems.put(item.getId(), item);
            }
        }

        // Slots all registered GuiItems into SlottedGuiItems
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

                item.slot(slot);
            }
        }

        return this;
    }

    /**
     * Loads all macros for this page.
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
            if (!type.equals(ActionType.EVENT))
            {
                for (String action : actionList.getStringList(type.toString()))
                    this.setMacro(action, new ActionTrigger(type));
                continue;
            }

            ConfigurationSection events = actionList.getConfigurationSection(type.toString());
            if (events == null) continue;

            for (String eventId : events.getValues(false).keySet())
            {
                for (String action : events.getStringList(eventId))
                {
                    ActionTrigger trigger = new ActionTrigger(type, eventId);
                    this.setMacro(action, trigger);
                }
            }
        }
    }

    /**
     * @param action The raw action arguments.
     * @param trigger The trigger to run the action on.
     * @since 0.5.0
     */
    private void setMacro(String action, ActionTrigger trigger)
    {
        Optional<Macro> optionalMacro = this.api.getMacroManager().getMacro(action);
        if (optionalMacro.isEmpty())
        {
            this.api.getLogger()
                    .warning("<" + this.getGui().getId() + ".yml> Unknown macro used: " + action);
            return;
        }
        this.addAction(new GuiAction<>(this, optionalMacro.get(), trigger, action));
    }

    /**
     * Updates the inventory with the current SlottedGuiItems.
     * @since 0.4.2
     */
    public void update()
    {
        for (SlottedGuiItem item : this.slottedItems)
            inventory.setItem(item.getSlot(), item.getItemStack());
    }

    /**
     * Updates the inventory with the current SlottedGuiItems.
     * @param player The player to parse placeholders for.
     * @since 0.4.2
     */
    public void update(OfflinePlayer player)
    {
        for (SlottedGuiItem item : this.slottedItems)
        {
            item.updateItem(player);
            inventory.setItem(item.getSlot(), item.getItemStack());
        }
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

        this.inventory.setItem(item.getSlot(), item.getItemStack());
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

    @Override
    public ConfigurationSection getSection()
    {
        return getConfig();
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
     * @return The inventory constructed from the pattern.
     * @since 0.1.0
     */
    public @NotNull Inventory getInventory(HumanEntity entity)
    {
        if (!(entity instanceof OfflinePlayer player))
            return getInventory();

        String title = PlaceholderAPIHook.parse(player, this.properties.parsePropertyPlaceholders(this.title));

        this.inventory = Bukkit.createInventory(
                this,
                Math.min(9 * this.pattern.size(), 54),
                ChatManager.colorize(title));

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
     * Open the GUI page for an entity, starting a new session.
     * @param entity The entity to open the GUI page for.
     * @since 0.4.0
     */
    public void open(HumanEntity entity)
    {
        this.open(entity, true);
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
        entity.openInventory(this.getInventory(entity));

        if (newSession || !viewerUUIDs.contains(entity.getUniqueId()))
            GuiSession.start(this.api, this, entity);
    }
}
