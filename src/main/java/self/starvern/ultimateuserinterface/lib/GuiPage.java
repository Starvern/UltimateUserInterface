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

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class GuiPage
{
    private final Gui gui;
    private final List<String> pattern;
    private final List<GuiItem> items;
    private final Inventory inventory;
    private final Map<String, Boolean> splitItems;

    public GuiPage(Gui gui, List<String> pattern)
    {
        this.gui = gui;
        this.pattern = pattern;
        this.items = new ArrayList<>();
        this.inventory = Bukkit.createInventory(null, 9 * this.pattern.size(), this.gui.getTitle());
        this.splitItems = new HashMap<>();

        this.loadItems();
    }

    /**
     * @return The GUI this page is inside.
     * @since 0.1.0
     */
    public Gui getGui()
    {
        return this.gui;
    }

    /**
     * Creates a clean duplicate of the page.
     * @return The new instance of GuiPage.
     * @since 0.1.5
     */
    public GuiPage duplicate()
    {
        return new GuiPage(this.gui, this.pattern);
    }

    /**
     * Constructs a list of GuiItems based on the pattern
     * @since 0.1.0
     */
    public void loadItems()
    {
        for (String letter : this.gui.getConfig().getConfigurationSection("").getKeys(false))
        {
            if (letter.length() != 1) continue;

            this.items.add(new GuiItem(this.gui, letter));
        }
    }

    /**
     * @return The inventory constructed from the pattern.
     * @since 0.1.0
     */
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

                if (this.splitItems.containsKey(letter) && this.splitItems.get(letter))
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
     * Adds a list of generated items to the GUI's item list.
     * @param id The character the item is assigned to.
     * @since 0.1.0
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
        splitItems.put(id, true);
    }

    /**
     * @param id The character associated with the item.
     * @return The list of items.
     * @since 0.1.0
     */
    @NotNull
    public List<GuiItem> getAllInstances(@NotNull String id)
    {
        return this.items.stream()
                .filter(item -> item.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
    }

    /**
     * @param item The item to compare to the GuiItem.
     * @return The GuiItem, or if the item doesn't match anything, null.
     * @since 0.1.0
     */
    @Nullable
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

        for (GuiItem guiItem : this.items)
        {
            if (guiItem.getUniqueId().equals(uuid))
                return guiItem;
        }
        return null;
    }

    /**
     * @param id The character associated with the item.
     * @return The GuiItem associated with the id, or null.
     * @since 0.1.0
     */
    @Nullable
    public GuiItem getItem(String id)
    {
        for (GuiItem item : this.items)
        {
            if (item.getId().equalsIgnoreCase(id))
                return item;
        }
        return null;
    }
}
