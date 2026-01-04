package self.starvern.ultimateuserinterface.macros.impl;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
        if (!(event.getHuman() instanceof Player player))
            return;

        String rawCommand = String.join(" ", action.getArguments());

        if (action.getHolder() instanceof GuiItem item)
        {
            String command = item.getItemTemplate().parseAllPlaceholders(rawCommand, player);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            return;
        }

        if (action.getHolder() instanceof GuiPage page)
        {
            Component command = page.getProperties().parsePropertyPlaceholders(rawCommand, player);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), ChatManager.decolorize(command));
            return;
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPIHook.parse(player, rawCommand));
    }
}
