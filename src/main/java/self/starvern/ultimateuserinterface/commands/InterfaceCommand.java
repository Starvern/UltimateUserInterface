package self.starvern.ultimateuserinterface.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.managers.GuiManager;

import java.util.Optional;

public class InterfaceCommand implements CommandExecutor
{
    public InterfaceCommand()
    {
        PluginCommand command = UUI.getSingleton().getCommand("interface");
        if (command == null)
        {
            UUI.getSingleton().getLogger().severe("Invalid plugin.yml. Please re-install the plugin.");
            return;
        }
        command.setTabCompleter(new InterfaceCommandCompleter());
        command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage("You need to be a player to open a gui.");
            return false;
        }

        if (!sender.hasPermission("ui.command.interface"))
        {
            sender.sendMessage("Insufficient permission.");
            return false;
        }

        if (args.length == 0)
        {
            player.sendMessage("You need to specify a gui name");
            return false;
        }

        if (args[0].equalsIgnoreCase("reload"))
        {
            if (!sender.hasPermission("ui.command.interface.reload"))
            {
                sender.sendMessage("Insufficient permission.");
                return false;
            }

            sender.sendMessage("Reloaded UltimateUserInterface.");
            UUI.getSingleton().load();
            return true;
        }

        Optional<Gui> guiOptional = GuiManager.getGui(args[0]);
        if (guiOptional.isEmpty())
        {
            player.sendMessage("Gui not found");
            return false;
        }

        int page = 0;

        if (args.length >= 2)
        {
            try
            {
                page = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException exception)
            {
                sender.sendMessage("Invalid page number. Defaulting to first page.");
            }
        }

        Player target = player;

        if (args.length >= 3)
        {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                if (onlinePlayer.getName().equalsIgnoreCase(args[1])) target = onlinePlayer;
            }
        }

        target.openInventory(guiOptional.get().getPage(page).getInventory());

        return true;
    }
}
