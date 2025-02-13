package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiCustomEvent;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

public class PlaceholderCompareNumberMacro extends Macro
{
    public PlaceholderCompareNumberMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "comparePlaceholderNum");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (action.getArguments().size() < 3) return;

        String passId = "comparePlaceholderPass";
        String failId = "comparePlaceholderFail";

        if (action.getArguments().size() > 3)
            passId = action.getArguments().get(3);

        if (action.getArguments().size() > 4)
            failId = action.getArguments().get(4);

        if (!(event.getHuman() instanceof Player player)) return;

        String placeholder = action.getArguments().get(0);
        String comparison = action.getArguments().get(1);
        String rawNumber = action.getArguments().get(2);

        String rawPlaceholderValue = PlaceholderAPIHook.parse(player, placeholder);
        double placeholderValue;
        double number;

        try
        {
            placeholderValue = Double.parseDouble(rawPlaceholderValue);
        } catch (NumberFormatException e)
        {
            event.getGui().getLogger().warning("[comparePlaceholderStr] Placeholder does not parse as a number.");
            return;
        }

        try
        {
            number = Double.parseDouble(rawNumber);
        } catch (NumberFormatException e)
        {
            event.getGui().getLogger().warning("[comparePlaceholderStr] Invalid number (" + rawNumber + ").");
            return;
        }

        boolean value = false;

        switch (comparison)
        {
            case "<" -> value = placeholderValue < number;
            case "<=" -> value = placeholderValue <= number;
            case ">" -> value = placeholderValue > number;
            case ">=" -> value = placeholderValue >= number;
            case "==" -> value = placeholderValue == number;
            case "!=" -> value = placeholderValue != number;
            default -> event.getGui().getLogger()
                    .warning("[comparePlaceholderStr] Invalid number comparison (" + comparison + ").");
        }

        String eventId = value ? passId : failId;
        @Nullable SlottedGuiItem item = null;

        if (action.getHolder() instanceof SlottedGuiItem itemBuffer)
            item = itemBuffer;

        GuiCustomEvent customEvent = new GuiCustomEvent(
                event.getHuman(),
                event.getPage(),
                eventId.replace("page::", ""),
                eventId.startsWith("page::") ? GuiCustomEvent.Type.PAGE: GuiCustomEvent.Type.ITEM,
                item
        );

        Bukkit.getPluginManager().callEvent(customEvent);
    }
}
