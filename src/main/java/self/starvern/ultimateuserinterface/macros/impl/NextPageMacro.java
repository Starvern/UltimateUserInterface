package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiClickEvent;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

import java.util.Optional;

public class NextPageMacro extends Macro
{
    public NextPageMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "nextpage");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (!event.getPage().isLast())
        {
            if (event instanceof GuiClickEvent)
                event.getPage().next().open(event.getHuman());
            return;
        }

        if (action.getArguments().size() == 0 || !(action.getHolder() instanceof GuiItem item))
            return;

        String character = action.getArguments().get(0);

        Optional<GuiItem> optionalItem = event.getPage().getItems().stream()
                .filter(guiItem -> guiItem.getId().equalsIgnoreCase(character))
                .findFirst();

        if (optionalItem.isEmpty()) return;

        item.setItem(optionalItem.get().getItem());
    }
}
