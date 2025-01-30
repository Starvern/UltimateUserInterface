package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiCustomEvent;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

public class CustomEventMacro extends Macro
{
    public CustomEventMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "callEvent");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (action.getArguments().isEmpty()) return;

        String id = action.getArguments().get(0);

        GuiCustomEvent customEvent = new GuiCustomEvent(event.getHuman(), event.getPage(), id);
        Bukkit.getPluginManager().callEvent(customEvent);
    }
}
