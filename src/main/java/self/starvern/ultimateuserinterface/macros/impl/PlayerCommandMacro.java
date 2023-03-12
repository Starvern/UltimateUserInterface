package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.managers.ChatManager;

/**
 * Force the player to run a command.
 */
public class PlayerCommandMacro extends Macro
{
    public PlayerCommandMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "playercommand");
    }

    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        this.performCommand((Player) event.getHuman(), String.join(" ", action.getArguments()));
    }

    private void performCommand(Player player, String text)
    {
        player.performCommand(ChatManager.colorize(text));
    }
}
