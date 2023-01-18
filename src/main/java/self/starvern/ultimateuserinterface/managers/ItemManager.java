package self.starvern.ultimateuserinterface.managers;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.List;

public class ItemManager
{
    /**
     * Builds an item based on a configuration section
     * @param config the config to use
     * @param path the section to reference
     * @return the built item
     */
    public static ItemUtility buildItem(FileConfiguration config, String path)
    {
        String name = config.getString(path + ".name", "UNKNOWN");
        String materialName = config.getString(path + ".material", "STONE");
        boolean enchanted = config.getBoolean(path + ".enchanted", false);
        List<String> lore = config.getStringList(path + ".lore");

        Material material;

        try
        {
            material = Material.valueOf(materialName);
        }
        catch (IllegalArgumentException exception)
        {
            material = Material.STONE;
        }

        /*
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return item;
         */

        return new ItemUtility(material)
                .addDisplayName(name)
                .addLore(lore)
                .makeEnchanted(enchanted);
    }
}
