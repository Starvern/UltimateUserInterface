package self.starvern.ultimateuserinterface.macros.impl;

import org.bukkit.plugin.Plugin;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;
import self.starvern.ultimateuserinterface.macros.GuiAction;
import self.starvern.ultimateuserinterface.macros.Macro;
import self.starvern.ultimateuserinterface.properties.impl.StringProperty;

import java.util.Locale;

/**
 * <p>
 *     To set any properties on a GuiItem, simply pass the id.
 *     To set any properties on the GuiPage, pass an id starting with 'page::'.
 *     Example: a page with property 'name' will be 'page::name'.
 * </p>
 * @since 0.5.0
 */
public class SetPropertyMacro extends Macro
{
    public SetPropertyMacro(UUI api, Plugin plugin)
    {
        super(api, plugin, "setProp");
    }

    @Override
    public void run(GuiEvent event, GuiAction<? extends GuiBased> action)
    {
        if (action.getArguments().size() < 2) return;

        String key = action.getArguments().get(0);
        String value = String.join(" ", action.getArguments().subList(1, action.getArguments().size()));

        if (key.toLowerCase(Locale.ROOT).startsWith("page::"))
        {
            key = key.replace("page::", "");
            StringProperty stringProp = new StringProperty(key, value);
            event.getPage().getProperties().setProperty(stringProp, true);
            return;
        }

        StringProperty stringProp = new StringProperty(key, value);

        if (action.getHolder() instanceof GuiPage page)
            page.getProperties().setProperty(stringProp, true);
        if (action.getHolder() instanceof SlottedGuiItem item)
            item.getProperties().setProperty(stringProp, true);

    }
}
