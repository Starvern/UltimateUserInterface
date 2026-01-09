package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.lib.GuiContext;

public class ColorField extends ItemField<Color, String>
{
    public ColorField(ItemTemplate template, ColorFieldType fieldType, String primitive)
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, GuiContext context)
    {
        @Nullable Color color = this.fieldType.getComplex(
                this.primitive,
                s -> context.parseAllPlaceholders(s, context.getPlayer())
        );

        if (color == null || !(itemStack.getItemMeta() instanceof LeatherArmorMeta meta))
            return itemStack;

        meta.setColor(color);
        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
