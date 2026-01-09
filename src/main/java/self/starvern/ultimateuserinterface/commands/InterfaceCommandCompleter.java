package self.starvern.ultimateuserinterface.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.managers.GuiManager;

import java.util.ArrayList;
import java.util.List;

public class InterfaceCommandCompleter implements TabCompleter
{
    private final UUIPlugin plugin;

    public InterfaceCommandCompleter(UUIPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                                      @NotNull String @NotNull [] args)
    {
        GuiManager guiManager = plugin.getApi().getGuiManager();
        List<String> suggestions = new ArrayList<>();
        /*


        if (!sender.hasPermission("uui.command.interface"))
            return suggestions;

        if (args.length == 1)
        {
            if (sender.hasPermission("uui.command.interface.reload"))
                suggestions.add("reload");

            for (Gui gui : guiManager.getGuis())
                suggestions.add(gui.getId());
        }

        if (args.length == 2)
        {
            Optional<Gui> guiOptional = guiManager.getGui(args[0]);
            if (guiOptional.isEmpty()) return suggestions;

            for (int page = 0; page < guiOptional.get().getPages().size(); page++)
                suggestions.add(String.valueOf(page));
        }

        if (args.length == 3)
        {
            for (Player player : Bukkit.getOnlinePlayers())
                suggestions.add(player.getName());
        }

         */

        return suggestions;
    }
}
