package self.starvern.ultimateuserinterface.lib;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.macros.ActionType;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * <p>
 *     This class represents a reference to an ItemStack,
 *     with events strapped to it.
 * </p>
 * @since 0.4.2
 */
public class GuiItem extends Actionable<GuiItem> implements GuiBased
{
    private final UUI api;
    private final GuiPage page;
    private final String id;
    private final UUID uuid = UUID.randomUUID();
    private final ConfigurationSection section;

    private ItemStack item;

    public GuiItem(UUI api, GuiPage page, String id)
    {
        super();
        this.api = api;
        this.page = page;
        this.id = id;
        ConfigurationSection section = this.page.getGui().getConfig().getConfigurationSection(this.id);
        if (section== null) section = this.page.getConfig().createSection(this.id);
        this.section = section;
        this.item = ItemUtility.build(this.api, this.getGui().getFile(), section);
    }

    /**
     * @return The Gui this item appears in.
     * @since 0.4.0
     */
    @Override
    public Gui getGui()
    {
        return this.page.getGui();
    }

    /**
     * Loads all macros for this item.
     * @since 0.4.0
     */
    @Override
    public void loadActions()
    {
        this.clearActions();
        ConfigurationSection actionList = this.section.getConfigurationSection("actions");

        if (actionList == null)
            return;

        for (ActionType type : ActionType.values())
        {
            for (String action : actionList.getStringList(type.toString()))
            {
                Optional<Macro> optionalMacro = this.api.getMacroManager().getMacro(action);
                if (optionalMacro.isEmpty()) continue;
                actions.add(new GuiAction<>(this, optionalMacro.get(), type, action));
            }
        }
    }

    /**
     * Slots this item into the page.
     * @param slot The slot to place this item into.
     * @return The slotted item.
     * @since 0.4.2
     */
    public SlottedGuiItem slot(int slot) {
        SlottedGuiItem slottedItem = new SlottedGuiItem(this.api, this, slot);
        slottedItem.loadActions();
        this.page.setItem(slottedItem);
        return slottedItem;
    }

    /**
     * @return The UUID for this item.
     * @since 0.4.0
     */
    public UUID getUniqueId()
    {
        return this.uuid;
    }

    /**
     * @return The GuiPage this item appears in.
     * @since 0.4.0
     */
    public GuiPage getPage()
    {
        return this.page;
    }

    /**
     * @return The character associated with this item.
     * @since 0.4.0
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Restores the original ItemStack for this item.
     * @since 0.4.0
     */
    public void restoreItem()
    {
        this.item = ItemUtility.build(this.api, this.getGui().getFile(), section);
        this.loadActions();
    }

    /**
     * @return The ItemStack associated with this GuiItem.
     * @since 0.4.0
     */
    public ItemStack getItem()
    {
        ItemStack item = this.item.clone();

        if (!this.actions.isEmpty())
            ItemUtility.addUUID(this.api, item, uuid.toString());

        return item;
    }

    /**
     * Update this GuiItem's ItemStack
     *
     * @param itemStack The ItemStack to set the item to.
     * @since 0.4.0
     */
    public void setItem(ItemStack itemStack)
    {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null && itemMeta.getPersistentDataContainer().has(api.getKey()))
            ItemUtility.removeUUID(this.api, this.item);

        this.item = itemStack;
    }

    /**
     * Checks if the ItemStack is the same as the GuiItem's ItemStack.
     * @param item The ItemStack to check.
     * @return True if the ItemStack is the GuiItem's.
     */
    public boolean isItem(ItemStack item)
    {
        String localizedName = ItemUtility.getUUID(this.api, item);
        if (localizedName == null) return false;
        return localizedName.equalsIgnoreCase(this.uuid.toString());
    }

    /**
     * Executes all macros for this item.
     * @param event The event to use.
     * @since 0.4.0
     */
    public void execute(GuiEvent event)
    {
        List<GuiAction<GuiItem>> actions = new ArrayList<>(this.actions);
        for (GuiAction<GuiItem> action : actions)
            action.execute(event);
    }

    /**
     * <p>
     *     A shorthand for creating macros on the fly. This overloaded method defaults to ActionType.CLICK.
     *     This will add an anonymous action to the GuiItem.
     * </p>
     * @param consumer The GuiEvent and GuiAction to provide to the Macro#run method.
     * @since 0.4.1
     */
    public void execute(BiConsumer<GuiEvent, GuiAction<GuiItem>> consumer)
    {
        Macro macro = new Macro(this.api, this.api.getPlugin(), "") {
            @Override
            public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
            {
                if (action.getHolder() instanceof GuiItem)
                {
                    consumer.accept(event, (GuiAction<GuiItem>) action);
                }
            }
        };

        this.actions.add(new GuiAction<>(this, macro, ActionType.CLICK, ""));
    }

    /**
     * A shorthand for creating macros on the fly. This will add an anonymous action to the GuiItem.
     * @param consumer The GuiEvent and GuiAction to provide to the Macro#run method.
     * @since 0.4.1
     */
    public void execute(ActionType type, BiConsumer<GuiEvent, GuiAction<GuiItem>> consumer)
    {
        Macro macro = new Macro(this.api, this.api.getPlugin(), "") {
            @Override
            public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
            {
                if (action.getHolder() instanceof GuiItem)
                    consumer.accept(event, (GuiAction<GuiItem>) action);
            }
        };

        this.actions.add(new GuiAction<>(this, macro, type, ""));
    }
}

