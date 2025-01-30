package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiClickEvent;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;

import java.util.Optional;

/**
 * Paginates to the next page, or the first if the page is last.
 * @since 0.4.2
 */
public class NextPageMacro extends Macro
{
    public NextPageMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "nextPage");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (!event.getPage().isLast())
        {
            if (event instanceof GuiClickEvent)
                event.getPage().next().open(event.getHuman(), false);
            return;
        }

        if (action.getArguments().isEmpty() || !(action.getHolder() instanceof SlottedGuiItem item))
            return;

        String character = action.getArguments().get(0);

        Optional<GuiItem> optionalItem = event.getPage().getItem(character);
        if (optionalItem.isEmpty()) return;

        item.setItemStack(optionalItem.get().getItemStack());
        event.getPage().update();
    }
}
