package self.starvern.ultimateuserinterface.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.item.ItemConfig;

import javax.annotation.Nullable;

/**
 * As of 0.4.0, this is now a static utility class.
 */
public class ItemUtility
{
    public static ItemStack addUUID(UUI api, ItemStack item, String value)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return item;

        itemMeta.getPersistentDataContainer().set(api.getKey(), PersistentDataType.STRING, value);

        item.setItemMeta(itemMeta);
        return item;
    }

    @Nullable
    public static String getUUID(UUI api, ItemStack item)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return null;

        return itemMeta.getPersistentDataContainer()
                .get(api.getKey(), PersistentDataType.STRING);
    }

    public static void removeUUID(UUI api, ItemStack item)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        itemMeta.getPersistentDataContainer().remove(api.getKey());
        item.setItemMeta(itemMeta);
    }

    /**
     * Compares and updates an item based on its config.
     * @param player The player to parse for.
     * @param item The item to update.
     * @since 0.5.0
     */
    public static void parseItem(OfflinePlayer player, ItemStack item, ItemConfig config)
    {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null || player == null) return;

        itemMeta.setDisplayName(PlaceholderAPIHook.parse(player, itemMeta.getDisplayName()));
        itemMeta.setLore(PlaceholderAPIHook.parse(player, itemMeta.getLore()));

        item.setItemMeta(itemMeta);
    }
}