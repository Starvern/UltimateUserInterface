package self.starvern.ultimateuserinterface.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.macros.Macro;

public class InterfaceCommand
{
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand(UUI api)
    {
        return Commands.literal("interface")
                .requires(src -> src.getSender().hasPermission("uui.command.interface"))
                .then(Commands.literal("reload")
                        .requires(src -> src.getSender()
                                .hasPermission("uui.command.interface.reload")
                        )
                        .executes(ctx -> manageReload(ctx, api))
                )
                .then(Commands.literal("list")
                        .executes(ctx -> listMacros(ctx, api)))
                .then(Commands.argument("gui", new GuiCommandArgument(api))
                        .requires(src -> src.getExecutor() instanceof Player)
                        .executes(InterfaceCommand::manageGui)
                );
    }

    private static int listMacros(CommandContext<CommandSourceStack> ctx, UUI api)
    {
        for (Macro macro : api.getMacroManager().getMacros())
        {
            ctx.getSource().getSender().sendMessage(Component.text(macro.toString()));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int manageReload(CommandContext<CommandSourceStack> ctx, UUI api)
    {
        ctx.getSource().getSender().sendMessage("Reloaded UltimateUserInterface.");
        api.getPlugin().load();
        return Command.SINGLE_SUCCESS;
    }

    private static int manageGui(CommandContext<CommandSourceStack> ctx)
    {
        if (!(ctx.getSource().getSender() instanceof Player player))
            return Command.SINGLE_SUCCESS;

        Gui gui = ctx.getArgument("gui", Gui.class);

        if (gui.getPermission() != null && !player.hasPermission(gui.getPermission()))
        {
            player.sendMessage("No permission.");
            return Command.SINGLE_SUCCESS;
        }

        gui.loadPages(player);
        GuiPage page = gui.getPage(0);
        // TODO: Add Gui Arguments
        page.open(player);

        return Command.SINGLE_SUCCESS;
    }

    /*
    TODO: FULLY IMPLEMENT

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args)
    {
        GuiPage page = guiOptional.get().getPage(0);
        page.loadArguments();

        for (int i = 0; i < page.getArguments().size(); i++)
        {
            GuiArgument argument = page.getArguments().get(i);
            String value = argument.getDefaultValue();

            if (args.length <= i+1 && argument.isRequired())
            {
                player.sendMessage("Specify argument: " + argument.getId());
                return false;
            }

            if (args.length > i+1)
                value = args[i+1];

            argument.setValue(PlaceholderAPIHook.parse(player, page.getProperties().parsePropertyPlaceholders(value)));

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
    } */
}
