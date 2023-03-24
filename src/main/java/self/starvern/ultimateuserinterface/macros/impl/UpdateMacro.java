package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

public class UpdateMacro extends Macro
{
    public UpdateMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "update");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (action.getHolder() instanceof GuiPage page && action.getArguments().size() != 0)
        {
            for (GuiItem item : page.getItems(action.getArguments().get(0)))
                page.setItem(item);
        }
    }
}
