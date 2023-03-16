package self.starvern.ultimateuserinterface.item;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class ItemConfig implements Serializable
{
    private final SerializableSection section;

    private final String name;
    private final Material material;
    private final List<String> lore;
    private final int amount;

    private final String texture;
    private final String playerName;
    private final String hdbId;

    private final SerializableSection enchantments;
    private final List<String> itemFlags;

    public ItemConfig()
    {
        this.section = null;
        this.name = "";
        this.material = Material.AIR;
        this.lore = new ArrayList<>();
        this.amount = 1;
        this.texture = "";
        this.hdbId = "";
        this.playerName = "";
        this.enchantments = null;
        this.itemFlags = new ArrayList<>();
    }

    public ItemConfig(File file, ConfigurationSection section)
    {
        this.section = new SerializableSection(file, section.getCurrentPath());
        this.name = section.getString("name", "");
        this.material = Material.matchMaterial(section.getString("material", "AIR"));
        this.lore = section.getStringList("lore");
        this.amount = section.getInt("amount", 1);

        this.texture = section.getString("texture", "");
        this.hdbId = section.getString("hdb", "");
        this.playerName = section.getString("player", "");

        this.enchantments = new SerializableSection(file, section.getCurrentPath() + ".enchantments");
        this.itemFlags = section.getStringList("flags");
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    @NotNull
    public Material getMaterial()
    {
        return (material == null) ? Material.AIR : material;
    }

    @NotNull
    public List<String> getLore()
    {
        return lore;
    }

    public int getAmount()
    {
        return amount;
    }

    @Nullable
    public ConfigurationSection getSection()
    {
        return section.getSection();
    }

    @NotNull
    public String getHdbId()
    {
        return hdbId;
    }

    @NotNull
    public String getPlayerName()
    {
        return playerName;
    }

    @NotNull
    public String getTexture()
    {
        return texture;
    }

    /**
     * @param displayName The name of the enchantment.
     * @return The enchantment based on the name provided.
     * @since 0.3.7
     */
    private static Optional<Enchantment> getEnchant(String displayName)
    {
        return Arrays.stream(Enchantment.values())
                .filter(enchantment -> enchantment.getKey().getKey().equalsIgnoreCase(displayName))
                .findFirst();
    }


    public Map<Enchantment, Integer> getEnchantments()
    {
        Map<Enchantment, Integer> enchantments = new HashMap<>();

        if (this.enchantments == null)
            return enchantments;

        ConfigurationSection enchantmentSection = this.enchantments.getSection();

        if (enchantmentSection == null)
            return enchantments;

        for (String enchantName : enchantmentSection.getKeys(false))
        {
            Optional<Enchantment> enchantmentOptional = getEnchant(enchantName);
            if (enchantmentOptional.isEmpty()) continue;

            enchantments.put(enchantmentOptional.get(), enchantmentSection.getInt(enchantName));
        }

        return enchantments;
    }

    public List<ItemFlag> getItemFlags()
    {
        List<ItemFlag> flags = new ArrayList<>();

        for (String flagName : this.itemFlags)
        {
            ItemFlag flag;
            try
            {
                flag = ItemFlag.valueOf(flagName.toUpperCase(Locale.ROOT));
            }
            catch (IllegalArgumentException exception)
            {
                continue;
            }
            flags.add(flag);
        }

        return flags;
    }
}
