package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

/**
 * Close the player's GUI.
 * @since 0.4.2
 */
public class CloseMacro extends Macro
{
    public CloseMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "close");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        event.getGui().getSessions().removeIf(s -> s.getViewer().getUniqueId() == event.getHuman().getUniqueId());
        event.getHuman().closeInventory();
    }
}
