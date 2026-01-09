package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.lib.GuiContext;

import java.util.List;

public class ItemFlagField extends ItemField<ItemFlag[], List<String>>
{
    public ItemFlagField(
            ItemTemplate template,
            ItemFlagFieldType fieldType,
            List<String> primitive
    )
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, GuiContext context)
    {
        @NotNull ItemFlag[] flags = this.fieldType.getComplex(this.primitive);

        if (itemStack.isEmpty())
            return itemStack;

        itemStack.addItemFlags(flags);

        return itemStack;
    }
}
