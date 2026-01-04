package self.starvern.ultimateuserinterface.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiArgument;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.properties.GuiProperty;

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
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args)
    {
        if (!sender.hasPermission("uui.alias." + commandLabel))
        {
            sender.sendMessage("Insufficient permission.");
            return false;
        }

        if (commandLabel.equalsIgnoreCase("uui"))
        {
            sender.sendMessage("UltimateUserInterface by Starvern");
            return true;
        }

        if (!(sender instanceof Player player))
        {
            sender.sendMessage("You need to be a player to open a gui.");
            return false;
        }

        Gui gui = this.plugin.getApi().getGuiManager().getGui(commandLabel);
        if (gui == null)
        {
            player.sendMessage("Gui not found");
            return false;
        }

        if (gui.getPermission() != null && !player.hasPermission(gui.getPermission()))
        {
            player.sendMessage("No permission.");
            return false;
        }

        gui.loadPages(player);
        GuiPage page = gui.getPage(0);
        page.loadArguments();

        for (int i = 0; i < page.getArguments().size(); i++)
        {
            GuiArgument argument = page.getArguments().get(i);

            String value = argument.getDefaultValue();

            if (args.length <= i && argument.isRequired())
            {
                player.sendMessage("Specify argument: " + argument.getId());
                return false;
            }

            if (args.length > i)
                value = args[i];

            argument.setValue(PlaceholderAPIHook.parse(
                    player,
                    MiniMessage.miniMessage().serialize(page.getProperties().parsePropertyPlaceholders(value, player))
            ));

            try
            {
                GuiProperty<?> property = argument.asProperty();
                page.getProperties().setProperty(property, true);
            }
            catch (NumberFormatException exception)
            {
                player.sendMessage("Invalid argument: " + argument.getId() + ", required type: " + argument.getType());
                return false;
            }
        }

        page.open(player);
        return true;
    }

    @Override
    public @NotNull UUIPlugin getPlugin()
    {
        return this.api.getPlugin();
    }
}
