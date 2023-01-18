package self.starvern.ultimateuserinterface.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.persistence.PersistentDataType;
import self.starvern.ultimateuserinterface.managers.ChatManager;

import java.util.List;

public class ItemUtility
{
    private Material material;
    private String displayName;
    private List<String> lore;
    private boolean enchanted = false;
    private NamespacedKey key;
    private String value;

    public ItemUtility(Material material)
    {
        this.material = material;
    }

    public ItemStack build()
    {
        return build(1);
    }

    public ItemStack build(int amount)
    {
        ItemStack item = new ItemStack(this.material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return item;

        if (this.displayName != null)
            itemMeta.setDisplayName(ChatManager.colorize(displayName));
        if (this.lore != null)
            itemMeta.setLore(ChatManager.colorize(lore));
        if (this.enchanted)
        {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (this.key != null)
            itemMeta.getPersistentDataContainer().set(this.key, PersistentDataType.STRING, this.value);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemUtility addDisplayName(String name)
    {
        this.displayName = name;
        return this;
    }

    public ItemUtility addLore(List<String> lore)
    {
        this.lore = lore;
        return this;
    }

    public ItemUtility makeEnchanted()
    {
        this.enchanted = true;
        return this;
    }

    public Material getMaterial()
    {
        return material;
    }

    public ItemUtility makeEnchanted(boolean value)
    {
        this.enchanted = value;
        return this;
    }

    /**
     * Adds a key to the item, only Strings are supported currently
     * @param key The key to use
     * @param value The value of the data
     * @return The instance of ItemUtility
     */
    public ItemUtility addKey(NamespacedKey key, String value)
    {
        this.key = key;
        this.value = value;
        return this;
    }

    public ItemUtility setMaterial(Material material)
    {
        this.material = material;
        return this;
    }
}