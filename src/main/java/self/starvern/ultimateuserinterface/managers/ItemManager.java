package self.starvern.ultimateuserinterface.managers;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import self.starvern.ultimateuserinterface.utils.ItemUtility;

import java.util.List;

public class ItemManager
{
    /**
     * Builds an item based on a configuration section.
     * @param config The config to use.
     * @param path The section to reference.
     * @return The built item.
     * @since 0.1.0
     */
    public static ItemUtility buildItem(FileConfiguration config, String path)
    {
        String name = config.getString(path + ".name", "UNKNOWN");
        String materialName = config.getString(path + ".material", "AIR");
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

        return new ItemUtility(material)
                .addDisplayName(name)
                .addLore(lore)
                .makeEnchanted(enchanted);
    }
}
