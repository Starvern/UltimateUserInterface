package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;

public class ColorFieldType extends ItemFieldType<Color, Color>
{
    public ColorFieldType(UUI api)
    {
        super(api, "color");
    }

    @Override
    public @Nullable Color getComplex(Color primitive)
    {
        return primitive;
    }

    @Override
    public ItemField<Color, Color> fillField(ItemTemplate template, ConfigurationSection section)
    {
        return new ColorField(template, this, section.getColor(this.key));
    }
}
