package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;

public class AmountField extends ItemField<Integer, String>
{
    public AmountField(ItemTemplate template, AmountFieldType fieldType, String primitive)
    {
        super(template, fieldType, primitive);
    }

    @Override
    public ItemStack apply(ItemStack itemStack, OfflinePlayer player)
    {
        @Nullable Integer amount = this.fieldType.getComplex(
                this.primitive,
                s -> this.template.parseAllPlaceholders(s, player)
        );

        if (amount == null || itemStack.isEmpty())
            return itemStack;

        itemStack.setAmount(amount);

        return itemStack;
    }
}
