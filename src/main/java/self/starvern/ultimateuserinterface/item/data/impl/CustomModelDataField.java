package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.lib.GuiContext;

import java.util.List;

public class CustomModelDataField extends ItemField<Float, String>
{
    public CustomModelDataField(ItemTemplate template, CustomModelDataFieldType fieldType, String primitive)
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, GuiContext context)
    {
        @Nullable Float customModelData = this.fieldType.getComplex(
                this.primitive,
                s -> this.template.parseAllPlaceholders(s, context.getPlayer())
        );

        if (customModelData == null || itemStack.isEmpty())
            return itemStack;

        itemStack.editMeta(meta -> {
           CustomModelDataComponent modelData = meta.getCustomModelDataComponent();
           modelData.setFloats(List.of(customModelData));
           meta.setCustomModelDataComponent(modelData);
        });

        return itemStack;
    }
}
