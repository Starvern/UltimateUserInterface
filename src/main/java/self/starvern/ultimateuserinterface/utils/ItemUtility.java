package self.starvern.ultimateuserinterface.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import self.starvern.ultimateuserinterface.managers.ChatManager;

import java.util.List;

public class ItemUtility
{
    private final Material material;
    private String displayName;
    private List<String> lore;
    private boolean enchanted = false;

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

    public ItemUtility makeEnchanted(boolean value)
    {
        this.enchanted = value;
        return this;
    }
}