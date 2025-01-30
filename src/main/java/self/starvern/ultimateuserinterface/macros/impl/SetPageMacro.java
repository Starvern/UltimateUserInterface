package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

public class SetPageMacro extends Macro
{
    public SetPageMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "setPage");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (action.getArguments().isEmpty()) return;

        String rawPage = action.getArguments().get(0);
        Gui gui = event.getPage().getGui();
        int page;

        try
        {
            page = Integer.parseInt(rawPage);
        }
        catch (NumberFormatException e)
        {
            event.getGui().getLogger().warning("[setPage] Amount does not parse to int.");
            return;
        }

        gui.getPage(page).open(event.getHuman(), false);
    }
}
