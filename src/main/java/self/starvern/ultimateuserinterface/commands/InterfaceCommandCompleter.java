package self.starvern.ultimateuserinterface.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.managers.GuiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InterfaceCommandCompleter implements TabCompleter
{
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                                      @NotNull String[] args)
    {
        List<String> suggestions = new ArrayList<>();

        if (!sender.hasPermission("uui.command.interface"))
            return suggestions;

        if (args.length == 1)
        {
            if (sender.hasPermission("uui.command.interface.reload"))
                suggestions.add("reload");

            for (Gui gui : GuiManager.getGuis())
                suggestions.add(gui.getId());
        }

        if (args.length == 2)
        {
            Optional<Gui> guiOptional = GuiManager.getGui(args[0]);
            if (guiOptional.isEmpty()) return suggestions;

            for (int page = 0; page < guiOptional.get().getPages().size(); page++)
                suggestions.add(String.valueOf(page));
        }

        if (args.length == 3)
        {
            for (Player player : Bukkit.getOnlinePlayers())
                suggestions.add(player.getName());
        }

        return suggestions;
    }
}
