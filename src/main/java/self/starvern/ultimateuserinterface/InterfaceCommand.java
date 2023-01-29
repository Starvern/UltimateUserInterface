package self.starvern.ultimateuserinterface;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.managers.GuiManager;

import java.util.Optional;
import java.util.stream.Collectors;

public class InterfaceCommand implements CommandExecutor
{
    public InterfaceCommand()
    {
        PluginCommand command = UUI.getSingleton().getCommand("interface");
        if (command == null)
        {
            Bukkit.getLogger().severe("Invalid plugin.yml. Please re-install the plugin.");
            return;
        }
        command.setExecutor(this);
        command.setTabCompleter(new InterfaceCommandCompleter());
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

        Gui gui = GuiManager.getGui(args[0]);
        if (gui == null)
        {
            player.sendMessage("Gui not found");
            return false;
        }

        int page = (args.length >= 2) ? Integer.parseInt(args[1]) : 0;

        Player target = player;

        if (args.length >= 3)
        {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                if (onlinePlayer.getName().equalsIgnoreCase(args[1])) target = onlinePlayer;
            }
        }

        target.openInventory(gui.getPage(page).getInventory());

        return true;
    }
}
