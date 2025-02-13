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

import java.util.Locale;

public class PlaceholderCompareStringMacro extends Macro
{
    public PlaceholderCompareStringMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "comparePlaceholderStr");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (!(event.getHuman() instanceof Player player)) return;
        if (action.getArguments().size() < 3) return;

        String rawPlaceholder = action.getArguments().get(0);
        String compare = action.getArguments().get(1);
        String result = action.getArguments().get(2);

        String placeholder = PlaceholderAPIHook.parse(player, rawPlaceholder);

        boolean value = false;

        switch (compare.toLowerCase(Locale.ROOT))
        {
            case "equals" -> value = placeholder.equals(result);
            case "equalsignorecase" -> value = placeholder.equalsIgnoreCase(result);
            case "startswith" -> value = placeholder.startsWith(result);
            case "endswith" -> value = placeholder.endsWith(result);

            case "!equals" -> value = !placeholder.equals(result);
            case "!equalsignorecase" -> value = !placeholder.equalsIgnoreCase(result);
            case "!startswith" -> value = !placeholder.startsWith(result);
            case "!endswith" -> value = !placeholder.endsWith(result);

            default -> event.getGui().getLogger()
                    .warning("[comparePlaceholderStr] Invalid comparison (" + compare + ").");
        }

        String passId = "comparePlaceholderStrPass";
        String failId = "comparePlaceholderStrFail";

        if (action.getArguments().size() > 3)
            passId = action.getArguments().get(3);
        if (action.getArguments().size() > 4)
            failId = action.getArguments().get(4);

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
