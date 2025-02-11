package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

public class SetTitleMacro extends Macro
{
    public SetTitleMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "setTitle");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        String title = "";

        if (!action.getArguments().isEmpty())
            title = String.join(" ", action.getArguments());

        event.getPage().setTitle(title);
    }
}
