package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;

public class AmountFieldType extends ItemFieldType<Integer, String>
{
    public AmountFieldType(UUI api)
    {
        super(api, "amount");
    }

    @Override
    public @Nullable Integer getComplex(@Nullable String primitive)
    {
        if (primitive == null)
            return null;

        try
        {
            return Integer.parseInt(primitive);
        }
        catch (NumberFormatException exception)
        {
            return null;
        }
    }

    @Override
    public ItemField<Integer, String> fillField(ItemTemplate template, ConfigurationSection section)
    {
        return new AmountField(template, this, section.getString(this.key));
    }
}
