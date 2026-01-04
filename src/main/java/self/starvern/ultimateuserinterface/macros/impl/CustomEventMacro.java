package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiCustomEvent;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

public class CustomEventMacro extends Macro
{
    public CustomEventMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "callEvent");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (action.getArguments().isEmpty()) return;

        GuiCustomEvent.Type type = GuiCustomEvent.Type.ITEM;
        String id = action.getArguments().getFirst();

        if (id.startsWith("page::"))
        {
            type = GuiCustomEvent.Type.PAGE;
            id = id.replace("page::", "");
        }

        @Nullable SlottedGuiItem item = null;

        if (action.getHolder() instanceof SlottedGuiItem itemBuffer)
            item = itemBuffer;

        GuiCustomEvent customEvent = new GuiCustomEvent(event.getHuman(), event.getPage(), id, type, item);
        Bukkit.getPluginManager().callEvent(customEvent);
    }
}
