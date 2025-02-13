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

public class PermissionCheckMacro extends Macro
{
    public PermissionCheckMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "checkPerm");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (action.getArguments().isEmpty())
            return;

        String permission = action.getArguments().get(0);

        String passId = "checkPermPass";
        String failId = "checkPermFail";

        if (action.getArguments().size() > 1)
            passId = action.getArguments().get(1);

        if (action.getArguments().size() > 2)
            failId = action.getArguments().get(2);

        String eventId = event.getHuman().hasPermission(permission) ? passId : failId;
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
