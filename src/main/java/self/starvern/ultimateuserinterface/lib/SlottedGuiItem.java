package self.starvern.ultimateuserinterface.lib;

import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.macros.GuiAction;

import java.util.ArrayList;
import java.util.List;

// SlottedGuiItem

/**
 * A GuiItem attached to a slot.
 * @since 0.4.2
 */
public class SlottedGuiItem extends GuiItem
{
    private final int slot;

    public SlottedGuiItem(UUI api, GuiPage page, String id, int slot)
    {
        super(api, page, id);
        this.slot = slot;
    }

    public SlottedGuiItem(UUI api, GuiItem item, int slot)
    {
        super(api, item.getPage(), item.getId());
        this.slot = slot;
    }

    public int getSlot()
    {
        return this.slot;
    }
}
