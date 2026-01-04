package self.starvern.ultimateuserinterface.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import self.starvern.ultimateuserinterface.UUI;

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
}