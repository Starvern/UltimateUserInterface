package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

import java.util.Optional;

public class ViewPermissionMacro extends Macro
{
    public ViewPermissionMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "viewperm");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (action.getArguments().isEmpty() || !(action.getHolder() instanceof SlottedGuiItem item))
            return;

        String permission = action.getArguments().get(0);

        if (event.getHuman().hasPermission(permission))
            return;

        if (action.getArguments().size() < 2)
        {
            ItemStack airItem = new ItemStack(Material.AIR);
            item.setItem(airItem);
            return;
        }

        String itemId = action.getArguments().get(1);

        Optional<GuiItem> optionalItem = event.getPage().getItem(itemId);

        if (optionalItem.isEmpty())
            return;

        item.setItem(optionalItem.get().getItem());
        item.setActions(optionalItem.get().getActions());
    }
}
