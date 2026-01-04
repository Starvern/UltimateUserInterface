package self.starvern.ultimateuserinterface.lib;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.macros.ActionTrigger;
import self.starvern.ultimateuserinterface.macros.ActionType;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.properties.GuiProperties;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.*;

/**
 * <p>
 *     This class represents an ItemStack with events strapped to it.
 *     To edit items in a GuiPage, access SlottedGuiItem.
 * </p>
 * @since 0.4.2
 */
public class GuiItem extends Actionable<GuiItem> implements GuiBased
{
    protected final UUI api;
    protected final GuiPage page;
    protected final String id;
    protected final UUID uuid;
    protected final ConfigurationSection section;
    protected final GuiProperties<GuiItem> properties;
    protected final ItemTemplate template;
    protected final ItemStack itemStack;
    protected final boolean doUpdate;
    protected final Player player;

    public GuiItem(UUI api, GuiPage page, String id, Player player)
    {
        super();
        this.api = api;
        this.page = page;
        this.id = id;
        this.uuid = UUID.randomUUID();
        ConfigurationSection section = this.page.getGui().getConfig().getConfigurationSection(this.id);
        if (section == null) section = this.page.getConfig().createSection(this.id);
        this.section = section;
        this.properties = new GuiProperties<>(this);
        this.properties.loadProperties();

        this.template = new ItemTemplate(this);
        this.itemStack = this.template.build(player);

        this.doUpdate = section.getBoolean("update_item", false);
        this.player = player;
        this.loadActions();
    }

    public Player getPlayer()
    {
        return this.player;
    }

    /**
     * Allows items the ability to be updated by the {@link GuiPage}
     * @return True, if this item should be updated.
     * @since 0.6.2
     */
    public boolean doUpdate()
    {
        return this.doUpdate;
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
     * @return Any properties this item has.
     * @since 0.5.0
     */
    public GuiProperties<GuiItem> getProperties()
    {
        return this.properties;
    }

    /**
     * @return The template used to build its {@link ItemStack}.
     * @since 0.6.3
     */
    public ItemTemplate getItemTemplate()
    {
        return this.template;
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
     * @return The section of the item.
     * @since 0.4.0
     */
    @Override
    public ConfigurationSection getSection()
    {
        return this.section;
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
     * Sets a new macro.
     * @param action The raw action to use.
     * @param trigger The trigger to run the action for.
     * @since 0.5.0
     */
    private void setMacro(String action, ActionTrigger trigger)
    {
        @Nullable Macro macro = this.api.getMacroManager().getMacro(action);
        if (macro == null)
        {
            this.api.getLogger()
                    .warning("<" + this.page.getGui().getId() + ".yml> Unknown macro used: " + action);
            return;
        }
        this.addAction(new GuiAction<>(this, macro, trigger, action, this.player));
    }

    /**
     * Slots this item into the page and updates GuiPage slotted items.
     * @param slot The slot to place this item into.
     * @return The slotted item.
     * @since 0.4.2
     */
    public SlottedGuiItem slot(int slot)
    {
        SlottedGuiItem slottedItem = new SlottedGuiItem(this.api, this, slot, this.player);
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
     * @return The ItemStack associated with this GuiItem.
     * @since 0.4.0
     */
    public ItemStack getItemStack()
    {
        ItemStack item = this.itemStack.clone();

        if (!this.actions.isEmpty())
            ItemUtility.addUUID(this.api, item, this.uuid.toString());

        return item;
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
        // Standard for loop so that macros may alter arguments.
        for (int i = 0; i < this.actions.size(); i++)
            this.actions.get(i).execute(event);
    }
}

