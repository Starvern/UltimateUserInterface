package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.lib.GuiContext;

public class MaterialField extends ItemField<Material, String>
{
    public MaterialField(ItemTemplate template, MaterialFieldType fieldType, String primitive)
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, GuiContext context)
    {
        @Nullable Material material = this.getFieldType().getComplex(
                this.getPrimitive(),
                s -> this.getTemplate().parseAllPlaceholders(s, context.getPlayer())
        );

        if (material == null || !material.isItem())
            return itemStack;

        if (itemStack.isEmpty())
            return ItemStack.of(material);

        return itemStack.withType(material);
    }
}
