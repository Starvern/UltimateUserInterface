package self.starvern.ultimateuserinterface.item.data.impl;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentFieldType extends ItemFieldType<Map<Enchantment, Integer>, ConfigurationSection>
{
    public EnchantmentFieldType(UUI api)
    {
        super(api, "enchantments");
    }

    /**
     * @param displayName The name of the enchantment.
     * @return The enchantment based on the name provided.
     * @since 0.6.3
     */
    private @Nullable Enchantment getEnchant(String displayName)
    {
        for (Enchantment enchantment : RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT))
        {
            if (enchantment.getKey().getKey().equalsIgnoreCase(displayName)) return enchantment;
        }

        return null;
    }

    @Override
    public @Nullable Map<Enchantment, Integer> getComplex(@Nullable ConfigurationSection primitive)
    {
        Map<Enchantment, Integer> enchantments = new HashMap<>();

        if (primitive == null) return enchantments;

        for (String enchantName : primitive.getKeys(false))
        {
            @Nullable Enchantment enchantment = this.getEnchant(enchantName);
            if (enchantment == null) continue;

            enchantments.put(enchantment, primitive.getInt(enchantName));
        }

        return enchantments;
    }

    @Override
    public ItemField<Map<Enchantment, Integer>, ConfigurationSection> fillField(
            ItemTemplate template,
            ConfigurationSection section
    )
    {
        return new EnchantmentField(template, this, section.getConfigurationSection(this.key));
    }
}
