package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;

public class ColorField extends ItemField<Color, Color>
{
    public ColorField(ItemTemplate template, ColorFieldType fieldType, Color primitive)
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, OfflinePlayer player)
    {
        // TODO: Add placeholder support.

        @Nullable Color color = this.fieldType.getComplex(this.primitive);

        if (color == null || !(itemStack.getItemMeta() instanceof LeatherArmorMeta meta))
            return itemStack;

        meta.setColor(color);
        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
