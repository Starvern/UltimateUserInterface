package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import self.starvern.ultimateuserinterface.UUI;

import java.util.*;
import java.util.stream.Collectors;

public class GuiPage
{
    private final Gui gui;
    private final List<String> pattern;
    private final List<GuiItem> items;
    private final Inventory inventory;
    private boolean isSplit;

    public GuiPage(Gui gui, List<String> pattern)
    {
        this.gui = gui;
        this.pattern = pattern;
        this.items = new ArrayList<>();
        this.inventory = Bukkit.createInventory(null, 9 * this.pattern.size(), this.gui.getTitle());

        this.loadItems();
    }

    public Gui getGui()
    {
        return this.gui;
    }

    public void loadItems()
    {
        for (String letter : this.gui.getConfig().getConfigurationSection("").getKeys(false))
        {
            if (letter.length() != 1) continue;

            this.items.add(new GuiItem(this.gui, letter));
        }
    }

    public Inventory getInventory()
    {
        int slot = -1;
        Map<String, Integer> itemCount = new HashMap<>();

        for (String line : this.pattern)
        {
            for (char character : line.toCharArray())
            {
                String letter = String.valueOf(character);
                slot++;

                GuiItem item = getItem(letter);
                if (item == null) continue;

                if (this.isSplit)
                {
                    List<GuiItem> instances = getAllInstances(letter);
                    if (instances.isEmpty()) continue;

                    int index = itemCount.getOrDefault(letter, 0);
                    if (index >= instances.size()) continue;
                    if (instances.get(index).getItem().getMaterial().equals(Material.AIR)) continue;
                    item = instances.get(index);
                    itemCount.put(letter, ++index);
                }
                this.inventory.setItem(slot, item.getItem().build());
            }
        }
        return this.inventory;
    }

    /**
     * Adds a list of generated items to the GUI's item list
     * @param id The character the item is assigned to
     */
    public void splitInstances(@NotNull String id)
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
        this.isSplit = true;
    }

    @NotNull
    public List<GuiItem> getAllInstances(@NotNull String id)
    {
        return this.items.stream()
                .filter(item -> item.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
    }

    public GuiItem getItem(ItemStack item)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return null;

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (!container.has(new NamespacedKey(UUI.getSingleton(), "uui-item-id"), PersistentDataType.STRING))
            return null;

        String rawUUID = container.get(new NamespacedKey(UUI.getSingleton(), "uui-item-id"), PersistentDataType.STRING);
        UUID uuid = UUID.fromString(rawUUID);

        for (GuiItem guiItem : items)
        {
            if (guiItem.getUniqueId().equals(uuid))
                return guiItem;
        }
        return null;
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
}