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

    /**
     * @param amount The amount of the ItemStack.
     * @return The built item.
     * @since 0.1.0
     */
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

    /**
     * @param name The new name of the item.
     * @return The instance of ItemUtility.
     * @since 0.1.0
     */
    public ItemUtility addDisplayName(String name)
    {
        this.displayName = name;
        return this;
    }

    /**
     * @param lore The new lore of the item.
     * @return The instance of ItemUtility.
     * @since 0.1.0
     */
    public ItemUtility addLore(List<String> lore)
    {
        this.lore = lore;
        return this;
    }

    /**
     * Makes the item glow with a basic enchantment.
     * @return The instance of ItemUtility.
     * @since 0.1.0
     */
    public ItemUtility makeEnchanted()
    {
        this.enchanted = true;
        return this;
    }

    /**
     * @return The material of the item.
     * @since 0.1.0
     */
    public Material getMaterial()
    {
        return material;
    }

    /**
     * Makes the item enchanted based on the value.
     * @param value Whether to make it enchanted.
     * @return The instance of ItemUtiltiy.
     * @since 0.1.0
     */
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
     * @since 0.1.0
     */
    public ItemUtility addKey(NamespacedKey key, String value)
    {
        this.key = key;
        this.value = value;
        return this;
    }

    /**
     * Set the item's material.
     * @param material The material of the item.
     * @return The instance of ItemUtility.
     * @since 0.1.0
     */
    public ItemUtility setMaterial(Material material)
    {
        this.material = material;
        return this;
    }
}