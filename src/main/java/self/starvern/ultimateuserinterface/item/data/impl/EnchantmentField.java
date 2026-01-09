package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.lib.GuiContext;

import java.util.Map;

public class EnchantmentField extends ItemField<Map<Enchantment, Integer>, ConfigurationSection>
{
    public EnchantmentField(
            ItemTemplate template,
            EnchantmentFieldType fieldType,
            ConfigurationSection primitive
    )
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, GuiContext context)
    {
        // TODO: Add placeholder support.

        @Nullable Map<Enchantment, Integer> enchantments = this.fieldType.getComplex(this.primitive);

        if (enchantments == null || itemStack.isEmpty())
            return itemStack;

        itemStack.addUnsafeEnchantments(enchantments);

        return itemStack;
    }
}
