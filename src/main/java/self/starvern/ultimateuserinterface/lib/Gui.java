package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.managers.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Map<String, Integer> itemCount = new HashMap<>();

        for (String line : pattern)
        {
            for (char character : line.toCharArray())
            {
                String letter = String.valueOf(character);
                slot++;

                List<GuiItem> items = getAllInstances(letter);
                if (items.isEmpty()) continue;

                int index = itemCount.getOrDefault(letter, 0);
                if (index >= items.size()) continue;
                if (items.get(index).getItem().getMaterial().equals(Material.AIR)) continue;
                this.inventory.setItem(slot, items.get(index).getItem().build());
                itemCount.put(letter, ++index);
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

    /**
     * Adds a list of generated items to the GUI's item list
     * @param id The character the item is assigned to
     */
    public void createInstances(@NotNull String id)
    {
        for (String line : pattern)
        {
            for (char character : line.toCharArray())
            {
                String letter = String.valueOf(character);
                if (!letter.equalsIgnoreCase(id)) continue;
                this.items.add(getItem(letter).duplicate());
            }
        }
    }

    @NotNull
    public List<GuiItem> getAllInstances(@NotNull String id)
    {
        return this.items.stream()
                .filter(item -> item.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
    }
}

