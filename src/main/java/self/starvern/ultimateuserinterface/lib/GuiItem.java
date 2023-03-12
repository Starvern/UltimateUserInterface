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
        this.section = this.page.getGui().getConfig().getConfigurationSection(this.id);
        this.item = ItemUtility.build(section);
        this.actions = new ArrayList<>();

        if (this.section == null) return;

        for (ActionType type : ActionType.values())
        {
            for (String action : this.section.getStringList("actions." + type))
            {
                Optional<Macro> optionalMacro = this.api.getMacroManager().getMacro(action);
                if (optionalMacro.isEmpty()) continue;
                actions.add(new GuiAction<>(this, optionalMacro.get(), type, action));
            }
        }

        if (!this.actions.isEmpty())
            ItemUtility.addLocalizedName(this.api, this.item, uuid.toString());
    }

    @Override
    public Gui getGui()
    {
        return this.page.getGui();
    }

    public int getSlot()
    {
        return this.slot;
    }

    public UUID getUniqueId()
    {
        return this.uuid;
    }

    public GuiPage getPage()
    {
        return this.page;
    }

    public String getId()
    {
        return this.id;
    }

    public GuiItem reloadItem()
    {
        this.item = ItemUtility.build(section);
        if (!this.actions.isEmpty())
            ItemUtility.addLocalizedName(this.api, this.item, uuid.toString());
        return this;
    }

    public ItemStack getItem()
    {
        return this.item.clone();
    }

    public GuiItem setItem(ItemStack itemStack)
    {
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLocalizedName())
            ItemUtility.removedLocalizedName(this.api, this.item);
        this.item = itemStack;

        if (!this.actions.isEmpty())
            ItemUtility.addLocalizedName(this.api, this.item, uuid.toString());

        this.page.setItem(this);
        return this;
    }

    public List<GuiAction<GuiItem>> getActions()
    {
        return actions;
    }

    public GuiItem removeAction(GuiAction<GuiItem> action)
    {
        this.actions.remove(action);
        return this;
    }

    public boolean isItem(ItemStack item)
    {
        String localizedName = ItemUtility.getLocalizedName(this.api, item);
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
        {
            action.execute(event);
        }

        return this;
    }
}

