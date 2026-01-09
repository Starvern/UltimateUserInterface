package self.starvern.ultimateuserinterface.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.managers.LocaleKey;

import java.util.List;

public class GuiCommandExecutor extends Command implements PluginIdentifiableCommand
{
    private final UUI api;
    private final UUIPlugin plugin;

    public GuiCommandExecutor(UUI api, @NotNull String name)
    {
        super(name);
        this.api = api;
        this.plugin = this.api.getPlugin();
        List<String> guiNames = this.api.getGuiManager().getGuiIds();
        this.setAliases(guiNames);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String @NotNull [] args)
    {
        if (!sender.hasPermission("uui.alias." + commandLabel))
        {
            sender.sendMessage(
                    this.api.getLocaleManager()
                            .getEntry(LocaleKey.NO_PERMISSION)
                            .getComponent()
            );
            return false;
        }

        if (commandLabel.equalsIgnoreCase("uui"))
        {
            sender.sendMessage(
                    this.api.getLocaleManager()
                            .getEntry(LocaleKey.PLUGIN_INFO)
                            .getComponent()
            );
            return true;
        }

        if (!(sender instanceof Player player))
        {
            sender.sendMessage(
                    this.api.getLocaleManager()
                            .getEntry(LocaleKey.NO_PLAYER)
                            .getComponent()
            );
            return false;
        }

        if (commandLabel.startsWith("uui:"))
            commandLabel = commandLabel.replace("uui:", "");

        @Nullable Gui gui = this.plugin.getApi().getGuiManager().getGui(commandLabel);
        if (gui == null)
        {
            player.sendMessage(
                    this.api.getLocaleManager()
                            .getEntry(LocaleKey.GUI_INVALID)
                            .supplyPlaceholder(Placeholder.parsed("gui", commandLabel))
                            .getComponent()
            );
            return false;
        }

        if (gui.getPermission() != null && !player.hasPermission(gui.getPermission()))
        {
            sender.sendMessage(
                    this.api.getLocaleManager()
                            .getEntry(LocaleKey.NO_PERMISSION)
                            .getComponent()
            );
            return false;
        }

        gui.loadPages(player);
        gui.loadArguments();

        if (!gui.checkArguments(player, args))
            return false;

        gui.open(player);
        return true;
    }

    @Override
    public @NotNull UUIPlugin getPlugin()
    {
        return this.api.getPlugin();
    }
}
