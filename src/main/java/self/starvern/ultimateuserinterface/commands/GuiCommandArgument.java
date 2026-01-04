package self.starvern.ultimateuserinterface.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.lib.Gui;

import java.util.concurrent.CompletableFuture;

public class GuiCommandArgument implements CustomArgumentType.Converted<Gui, String>
{
    private final UUI api;

    public GuiCommandArgument(UUI api)
    {
        this.api = api;
    }

    private static final DynamicCommandExceptionType ERROR_INVALID_GUI = new DynamicCommandExceptionType(guiId ->
            MessageComponentSerializer.message().
                    serialize(Component.text(guiId + " is not a valid gui."))
    );

    @Override
    public @NotNull Gui convert(@NotNull String targetGuiId) throws CommandSyntaxException
    {
        for (String guiId : this.api.getGuiManager().getGuiIds())
        {
            if (guiId.equals(targetGuiId))
                return this.api.getGuiManager().getGui(guiId);
        }

        throw ERROR_INVALID_GUI.create(targetGuiId);
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        for (String guiId : this.api.getGuiManager().getGuiIds())
        {
            if (guiId.startsWith(builder.getRemainingLowerCase()))
                builder.suggest(guiId);
        }

        return builder.buildFuture();
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType()
    {
        return StringArgumentType.word();
    }
}
