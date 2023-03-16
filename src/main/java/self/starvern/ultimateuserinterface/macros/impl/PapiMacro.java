package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

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

        if (holder instanceof GuiItem item)
            this.parseItem(item, player);

        if (holder instanceof GuiPage page)
        {
            for (GuiItem item : page.getItems())
                this.parseItem(item, player);
        }
    }

    private void parseItem(GuiItem item, Player player)
    {
        ItemStack itemStack = item.getPage().getInventory().getItem(item.getSlot());

        if (itemStack == null || itemStack.getType().isAir())
            return;

        item.setItem(ItemUtility.parsePlaceholders(this.api, player, itemStack));
    }
}
