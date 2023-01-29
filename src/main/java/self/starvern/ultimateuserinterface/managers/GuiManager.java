package self.starvern.ultimateuserinterface.managers;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiPage;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class GuiManager
{
    private static final Set<Gui> guis = new HashSet<>();

    /**
     * Convert all the files in /gui/ to GUIs.
     * @since 0.1.0
     */
    public static void loadGuis()
    {
        File folder = new File(UUI.getSingleton().getDataFolder(), "gui");

        File[] files = folder.listFiles();
        if (files == null)
        {
            return;
        }

        for (File file : files)
        {
            guis.add(new Gui(file));
        }
    }

    /**
     * @param id The filename of the GUI file.
     * @return The GUI, or null.
     * @since 0.1.0
     */
    @Nullable
    public static Gui getGui(String id)
    {
        for (Gui gui : guis)
        {
            if (gui.getId().equalsIgnoreCase(id))
                return gui;
        }
        return null;
    }

    /**
     * @param inventory The inventory to check
     * @return The GuiPage this inventory is owned by, or null
     * @since 0.1.0
     */
    @Nullable
    public static GuiPage getGuiPage(Inventory inventory)
    {
        if (inventory == null) return null;
        for (Gui gui : guis)
        {
            for (GuiPage page : gui.getPages())
                if (page.getInventory().equals(inventory)) return page;
        }
        return null;
    }

    /**
     * @return A list of all loaded GUIs.
     * @since 0.1.7
     */
    public static Set<Gui> getGuis()
    {
        return guis;
    }
}
