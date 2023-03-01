package self.starvern.ultimateuserinterface.managers;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GuiManager
{
    private static final Set<Gui> guis = new HashSet<>();
    private static final Set<Gui> instances = new HashSet<>();

    /**
     * Convert all the files in /gui/ to GUIs.
     * @since 0.1.0
     */
    public static void loadGuis()
    {
        guis.clear();

        File folder = new File(UUI.getSingleton().getDataFolder(), "gui");

        File[] files = folder.listFiles();
        if (files == null)
        {
            return;
        }

        for (File file : files)
        {
            guis.add(new Gui(file).loadPages());
        }
    }

    /**
     * @return All duplicates of any Guis.
     */
    public static Set<Gui> getInstances()
    {
        return instances;
    }

    /**
     * @param id The filename of the GUI file.
     * @return A duplicated instance of GUI, or null.
     * @since 0.1.0
     */
    public static Optional<Gui> getGui(String id)
    {
        for (Gui gui : guis)
        {
            if (gui.getId().equalsIgnoreCase(id))
            {
                Gui duplicatedGui = gui.duplicate();
                instances.add(duplicatedGui);
                return Optional.of(duplicatedGui);
            }
        }
        return Optional.empty();
    }

    /**
     * @param inventory The inventory to check
     * @return The GuiPage this inventory is owned by, or null
     * @since 0.1.0
     */
    public static Optional<GuiPage> getGuiPage(Inventory inventory)
    {
        if (inventory == null)
            return Optional.empty();

        for (ItemStack item : inventory.getContents())
        {
            for (Gui gui : instances)
            {
                for (GuiPage page : gui.getPages())
                {
                    Optional<GuiItem> guiItemOptional = page.getItem(item);
                    if (guiItemOptional.isPresent())
                        return Optional.of(page);

                }
            }
        }
        return Optional.empty();
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
