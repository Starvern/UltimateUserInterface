package self.starvern.ultimateuserinterface.managers;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.lib.Gui;
import self.starvern.ultimateuserinterface.lib.GuiPage;

import java.io.File;
import java.util.HashSet;
import java.util.List;
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
     *
     * @param file The File to create the Gui from.
     * @return The created Gui.
     * @since 0.4.2
     */
    public Gui createGui(File file)
    {
        Gui gui = new Gui(this.api, file);
        this.guis.add(gui);
        return gui;
    }

    /**
     * Convert all the files in /gui/ to GUIs.
     * @since 0.1.0
     */
    public void loadGuis()
    {
        this.guis.clear();
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
            this.guis.add(new Gui(this.api, file));
        }
    }

    /**
     * @return All duplicates of any Guis.
     * @since 0.4.0
     */
    public Set<Gui> getInstances()
    {
        return this.instances;
    }

    /**
     * @param id The filename of the GUI file.
     * @return A duplicated instance of GUI, or null.
     * @since 0.1.0
     */
    public @Nullable Gui getGui(String id)
    {
        for (Gui gui : this.guis)
        {
            if (gui.getId().equalsIgnoreCase(id))
            {
                Gui duplicatedGui = gui.duplicate();
                this.instances.add(duplicatedGui);
                return duplicatedGui;
            }
        }
        return null;
    }

    /**
     * @param inventory The inventory to check
     * @return The GuiPage this inventory is owned by, or null
     * @since 0.1.0
     */
    public @Nullable GuiPage getGuiPage(Inventory inventory)
    {
        if (inventory != null && inventory.getHolder() instanceof GuiPage)
            return (GuiPage) inventory.getHolder();

        return null;
    }

    /**
     * @return A list of all loaded GUI ids.
     * @since 0.7.0
     */
    public List<String> getGuiIds()
    {
        return this.guis.stream().map(Gui::getId).toList();
    }
}
