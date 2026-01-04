package self.starvern.ultimateuserinterface.item.data.impl;

import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;

public class CustomNameField extends ItemField<Component, String>
{
    public CustomNameField(ItemTemplate template, CustomNameFieldType fieldType, String primitive)
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, OfflinePlayer player)
    {
        @Nullable Component component = this.fieldType.getComplex(
                this.primitive,
                s -> this.template.parseAllPlaceholders(s, player)
        );

        if (component == null || itemStack.isEmpty())
            return itemStack;

        itemStack.editMeta(meta -> meta.customName(component));

        return itemStack;
    }
}
