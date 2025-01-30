package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
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
        event.getPage().update((OfflinePlayer) event.getHuman());
    }
}
