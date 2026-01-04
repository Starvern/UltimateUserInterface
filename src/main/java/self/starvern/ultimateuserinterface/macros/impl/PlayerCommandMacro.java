package self.starvern.ultimateuserinterface.macros.impl;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
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

        String rawCommand = String.join(" ", action.getArguments());

        if (action.getHolder() instanceof GuiItem item)
        {
            String command = item.getItemTemplate().parseAllPlaceholders(rawCommand, player);
            player.performCommand(command);
            return;
        }

        if (action.getHolder() instanceof GuiPage page)
        {
            Component command = page.getProperties().parsePropertyPlaceholders(rawCommand, player);
            player.performCommand(ChatManager.decolorize(command));
            return;
        }

        player.performCommand(PlaceholderAPIHook.parse(player, rawCommand));
    }
}
