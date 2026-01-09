package self.starvern.ultimateuserinterface.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.managers.LocaleKey;

import java.util.concurrent.CompletableFuture;

public class GuiCommandArgument implements CustomArgumentType.Converted<Gui, String>
{
    private final UUI api;
    private final DynamicCommandExceptionType errorInvalidGui;

    public GuiCommandArgument(UUI api)
    {
        this.api = api;
        this.errorInvalidGui = new DynamicCommandExceptionType(guiId ->
                this.api.getLocaleManager().getEntry(LocaleKey.GUI_UNKNOWN).supplyPlaceholder(
                        Placeholder.parsed("gui", guiId.toString())
                ).getMessage()
        );
    }

    @Override
    public @NotNull Gui convert(@NotNull String targetGuiId) throws CommandSyntaxException
    {
        for (String guiId : this.api.getGuiManager().getGuiIds())
        {
            if (guiId.equals(targetGuiId))
            {
                @Nullable Gui gui = this.api.getGuiManager().getGui(guiId);
                if (gui == null)
                    throw this.errorInvalidGui.create(targetGuiId);
                return gui;
            }
        }

        throw this.errorInvalidGui.create(targetGuiId);
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
