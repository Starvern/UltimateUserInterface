package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

/**
 * Run a command through console, parses PlaceholderAPI placeholders.
 * @since 0.4.2
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
        String command = String.join(" ", action.getArguments());

        if (event.getHuman() instanceof Player player)
            command = PlaceholderAPIHook.parse(player, command);

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
