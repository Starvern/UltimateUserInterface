package self.starvern.ultimateuserinterface.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import self.starvern.ultimateuserinterface.managers.ChatManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ItemUtility
{
    private Material material;
    private String displayName;
    private List<String> lore;
    private boolean enchanted = false;
    private NamespacedKey key;
    private String value;
    private final List<ItemFlag> flags = new ArrayList<>();

    public ItemUtility(Material material)
    {
        this.material = material;
        this.flags.addAll(List.of(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES));
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
        if (!this.flags.isEmpty())
            itemMeta.addItemFlags(this.flags.toArray(new ItemFlag[0]));

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
     * Copies the values from the parameter to the instance.
     * @param item The item to copy from.
     * @return The instance of the item, which the values have been copied to.
     * @since 0.1.3
     */
    public ItemUtility copy(ItemUtility item)
    {
        this.setMaterial(item.getMaterial())
                .addDisplayName(item.getDisplayName())
                .addLore(item.getLore())
                .addKey(item.getKey(), item.getKeyValue());

        return this;
    }

    /**
     * @return The item's display name.
     * @since 0.1.3
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * * Set's the item's display name to the result of the function.
     * @param editorFunction A function to change the item's display name
     * @return The instance of ItemUtility.
     * @since 0.2.4
     */
    public ItemUtility updateDisplayName(Function<String, String> editorFunction)
    {
        this.displayName = editorFunction.apply(this.displayName);
        return this;
    }

    /**
     * @return The item's lore.
     * @since 0.1.3
     */
    public List<String> getLore()
    {
        return lore;
    }

    /**
     * @return The item's NamespacedKey
     * @since 0.1.3
     */
    public NamespacedKey getKey()
    {
        return key;
    }

    /**
     * @return Returns the data stored in the item's PDC
     * @since 0.1.3
     */
    public String getKeyValue()
    {
        return this.value;
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
     * Set's the item's lore to the result of the function.
     * @param editorFunction A function to change the item's lore
     * @return The instance of ItemUtility.
     * @since 0.2.4
     */
    public ItemUtility updateLore(Function<List<String>, List<String>> editorFunction)
    {
        this.lore.clear();
        this.lore.addAll(editorFunction.apply(this.lore));
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
     * @return True if the item is enchanted.
     * @since 0.2.5
     */
    public boolean isEnchanted()
    {
        return this.enchanted;
    }

    /**
     * @return The material of the item.
     * @since 0.1.0
     */
    public Material getMaterial()
    {
        return this.material;
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

    /**
     * @return ALl the item's flags.
     * @since 0.2.4
     */
    public List<ItemFlag> getFlags()
    {
        return flags;
    }

    /**
     * Set a flag for the item.
     * @param flag The flag to add.
     * @return The instance of ItemUtility.
     * @since 0.2.4
     */
    public ItemUtility addFlag(ItemFlag flag)
    {
        flags.add(flag);
        return this;
    }

    /**
     * Set multiple flag for the item.
     * @param flags The flags to add.
     * @return The instance of ItemUtility.
     * @since 0.2.4
     */
    public ItemUtility addFlag(ItemFlag ... flags)
    {
        this.flags.addAll(List.of(flags));
        return this;
    }

    /**
     * Set and replace all flags.
     * @param flags The flags to set.
     * @return The instance of ItemUtility.
     * @since 0.2.4
     */
    public ItemUtility setFlags(ItemFlag ... flags)
    {
        this.flags.clear();
        this.flags.addAll(List.of(flags));
        return this;
    }

}