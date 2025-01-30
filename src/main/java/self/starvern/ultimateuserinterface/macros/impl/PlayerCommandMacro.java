package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.managers.ChatManager;

/**
 * Force the player to run a command.
 * @since 0.4.2
 */
public class PlayerCommandMacro extends Macro
{
    public PlayerCommandMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "playerCommand");
    }

    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (!(event.getHuman() instanceof Player player))
            return;

        String command = PlaceholderAPIHook.parse(
                player,
                String.join(" ", action.getArguments())
        );

        player.performCommand(ChatManager.colorize(command));
    }
}
