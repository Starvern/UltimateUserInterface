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
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (!(sender instanceof Player player))
        {
            sender.sendMessage("You need to be a player to open a gui.");
            return false;
        }

        if (args.length == 0)
        {
            player.sendMessage("You need to specify a gui name");
            return false;
        }

        Gui gui = GuiManager.getGui(args[0]);
        if (gui == null)
        {
            player.sendMessage("Gui not found");
            return false;
        }

        player.openInventory(gui.getInventory());

        return true;
    }
}
