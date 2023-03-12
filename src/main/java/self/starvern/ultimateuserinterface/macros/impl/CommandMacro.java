package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

/**
 * Run a command through console.
 */
public class CommandMacro extends Macro
{
    public CommandMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "command");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        this.runCommand(String.join(" ", action.getArguments()));
    }

    private void runCommand(String command)
    {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
