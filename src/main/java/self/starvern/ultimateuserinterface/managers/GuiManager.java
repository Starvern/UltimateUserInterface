package self.starvern.ultimateuserinterface.managers;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.UUIPlugin;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GuiManager
{
    private final UUI api;

    private final Set<Gui> guis;
    private final Set<Gui> instances;

    public GuiManager(UUI api)
    {
        this.api = api;
        guis = new HashSet<>();
        instances = new HashSet<>();
    }

    /**
     * Convert all the files in /gui/ to GUIs.
     * @since 0.1.0
     */
    public void loadGuis()
    {
        guis.clear();

        File folder = new File(this.api.getPlugin().getDataFolder(), "gui");
        loadFiles(folder.listFiles());
    }

    /**
     * Loads all files in the folder as GUIs.
     * @param files The files to load.
     * @since 0.4.0
     */
    private void loadFiles(File[] files)
    {
        if (files == null) return;

        for (File file : files)
        {
            if (file.isDirectory())
                this.loadFiles(file.listFiles());
            guis.add(new Gui(this.api, file).loadPages());
        }
    }

    /**
     * @return All duplicates of any Guis.
     * @since 0.4.0
     */
    public Set<Gui> getInstances()
    {
        return instances;
    }

    /**
     * @param id The filename of the GUI file.
     * @return A duplicated instance of GUI, or null.
     * @since 0.1.0
     */
    public Optional<Gui> getGui(String id)
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
    public Optional<GuiPage> getGuiPage(Inventory inventory)
    {
        if (inventory == null)
            return Optional.empty();

        if (inventory.getHolder() instanceof GuiPage)
            return Optional.of((GuiPage) inventory.getHolder());

        return Optional.empty();
    }

    /**
     * @return A list of all loaded GUIs.
     * @since 0.1.7
     */
    public Set<Gui> getGuis()
    {
        return guis;
    }
}
