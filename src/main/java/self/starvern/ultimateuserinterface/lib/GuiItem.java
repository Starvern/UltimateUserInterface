package self.starvern.ultimateuserinterface.lib;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.macros.ActionType;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.*;

/**
 * <p>
 *     This class represents a reference to an ItemStack,
 *     with events strapped to it.
 * </p>
 */
public class GuiItem implements GuiBased
{
    private final UUI api;
    private final GuiPage page;
    private final String id;
    private final int slot;
    private final UUID uuid = UUID.randomUUID();
    private final ConfigurationSection section;

    private final List<GuiAction<GuiItem>> actions;

    private ItemStack item;

    public GuiItem(UUI api, GuiPage page, String id, int slot)
    {
        this.api = api;
        this.page = page;
        this.id = id;
        this.slot = slot;
        ConfigurationSection section = this.page.getGui().getConfig().getConfigurationSection(this.id);
        if (section== null) section = this.page.getConfig().createSection(this.id);
        this.section = section;
        this.item = ItemUtility.build(this.api, this.getGui().getFile(), section);
        this.actions = new ArrayList<>();
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
     * @return The instance of GuiItem.
     * @since 0.4.0
     */
    public GuiItem loadActions()
    {
        this.actions.clear();
        for (ActionType type : ActionType.values())
        {
            for (String action : this.section.getStringList("actions." + type))
            {
                Optional<Macro> optionalMacro = this.api.getMacroManager().getMacro(action);
                if (optionalMacro.isEmpty()) continue;
                actions.add(new GuiAction<>(this, optionalMacro.get(), type, action));
            }
        }
        return this;
    }

    /**
     * @return The slot in the inventory this item appears in.
     * @since 0.4.0
     */
    public int getSlot()
    {
        return this.slot;
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
     * @return The instance of GuiItem.
     * @since 0.4.0
     */
    public GuiItem restoreItem()
    {
        this.item = ItemUtility.build(this.api, this.getGui().getFile(), section);
        return this;
    }

    public ItemStack getItem()
    {
        ItemStack item = this.item.clone();

        if (!this.actions.isEmpty())
            ItemUtility.addUUID(this.api, item, uuid.toString());

        return item;
    }

    public GuiItem setItem(ItemStack itemStack)
    {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLocalizedName())
            ItemUtility.removeUUID(this.api, this.item);
        this.item = itemStack;

        this.page.setItem(this);
        return this;
    }

    public List<GuiAction<GuiItem>> getActions()
    {
        return actions;
    }

    public GuiItem setActions(List<GuiAction<GuiItem>> actions)
    {
        this.actions.clear();
        this.actions.addAll(actions);
        return this;
    }

    public GuiItem removeAction(GuiAction<GuiItem> action)
    {
        this.actions.remove(action);
        return this;
    }

    public GuiItem addAction(GuiAction<GuiItem> action)
    {
        this.actions.add(action);
        return this;
    }

    public boolean isItem(ItemStack item)
    {
        String localizedName = ItemUtility.getUUID(this.api, item);
        if (localizedName == null) return false;
        return localizedName.equalsIgnoreCase(this.uuid.toString());
    }

    /**
     * Executes all macros for this item.
     * @param event The event to use.
     * @return The instance of GuiItem.
     * @since 0.4.0
     */
    public GuiItem execute(GuiEvent event)
    {
        List<GuiAction<GuiItem>> actions = new ArrayList<>(this.actions);
        for (GuiAction<GuiItem> action : actions)
            action.execute(event);

        return this;
    }
}

