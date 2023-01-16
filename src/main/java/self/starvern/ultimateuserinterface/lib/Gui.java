package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import self.starvern.ultimateuserinterface.managers.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Gui
{
    private final File file;
    private final FileConfiguration config;

    private final String id;
    private final String title;
    private final List<String> pattern;
    private final List<GuiItem> items;
    private final Inventory inventory;

    public Gui(File file)
    {
        this.file = file;
        this.config = FileManager.getConfig(file);
        this.id = file.getName().replace(".yml", "");
        this.title = this.config.getString("title", "Gui");
        this.pattern = this.config.getStringList("pattern");
        this.items = new ArrayList<>();
        this.inventory = Bukkit.createInventory(null, 9 * this.pattern.size(), this.title);

        Bukkit.getLogger().info(this.id);

        this.loadItems();
    }

    public File getFile()
    {
        return file;
    }

    public FileConfiguration getConfig()
    {
        return config;
    }

    public String getId() {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public List<String> getPattern()
    {
        return pattern;
    }

    public List<GuiItem> getItems()
    {
        return items;
    }

    public void loadItems()
    {
        for (String letter : this.config.getConfigurationSection("").getKeys(false))
        {
            if (letter.length() != 1) continue;

            items.add(new GuiItem(this, letter));
        }
    }

    public GuiItem getItem(String id)
    {
        for (GuiItem item : items)
        {
            if (item.getId().equalsIgnoreCase(id))
                return item;
        }
        return null;
    }

    public Inventory getInventory()
    {
        int slot = -1;

        for (String line : pattern)
        {
            for (char letter : line.toCharArray())
            {
                String character = String.valueOf(letter);
                slot++;

                GuiItem item = getItem(character);
                if (item == null) continue;

                this.inventory.setItem(slot, item.getItem());
            }
        }

        return this.inventory;
    }

    public GuiItem getItem(ItemStack item)
    {
        for (GuiItem guiItem : items)
        {
            if (guiItem.getItem().equals(item))
                return guiItem;
        }
        return null;
    }
}
