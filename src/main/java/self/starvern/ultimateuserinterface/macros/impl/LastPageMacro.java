package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

public class LastPageMacro extends Macro
{
    public LastPageMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "lastpage");
    }


    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        event.getPage().last().open(event.getHuman());
    }
}
