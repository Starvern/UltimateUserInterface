package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiClickEvent;
import self.starvern.ultimateuserinterface.api.GuiDragEvent;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

/**
 * Prevents the item from being moved or taken from the GUI.
 * @since 0.4.2
 */
public class StaticMacro extends Macro
{
    public StaticMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "static");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (event instanceof GuiClickEvent clickEvent)
            clickEvent.setCancelled(true);
        if (event instanceof GuiDragEvent dragEvent)
            dragEvent.setCancelled(true);
    }
}
