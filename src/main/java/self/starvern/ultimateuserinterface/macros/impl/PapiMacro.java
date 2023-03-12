package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiClickEvent;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.Optional;

/**
 * <p>
 *     This macro will parse all PlaceholderAPI placeholders
 *     for the original GuiItem only. Once this item is removed,
 *     the functionality is stripped.
 * </p>
 */
public class PapiMacro extends Macro
{
    private final UUI api;

    public PapiMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "papi");
        this.api = api;
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        GuiBased holder = action.getHolder();
        Player player = (Player) event.getHuman();

        // Fix to only parse if item in slot == guiItem

        if (holder instanceof GuiItem item)
        {
            ItemStack itemStack = event.getPage().getInventory().getItem(item.getSlot());
            if (itemStack == null || itemStack.getType().isAir())
                return;

            if (!item.isItem(itemStack) || event instanceof GuiClickEvent)
            {
                ItemUtility.removedLocalizedName(this.api, item.getItem());
                item.removeAction((GuiAction<GuiItem>) action);
                return;
            }

            this.parseItem(item, player);
        }

        if (holder instanceof GuiPage page)
        {
            for (GuiItem item : page.getItems())
                this.parseItem(item, player);
        }
    }

    private void parseItem(GuiItem item, Player player)
    {
        item.reloadItem();
        item.setItem(ItemUtility.parsePlaceholders(player, item.getItem()));
    }
}
