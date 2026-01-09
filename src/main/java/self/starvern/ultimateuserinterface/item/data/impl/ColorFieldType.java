package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;

import java.util.HexFormat;

public class ColorFieldType extends ItemFieldType<Color, String>
{
    public ColorFieldType(UUI api)
    {
        super(api, "color");
    }

    @Override
    public @Nullable Color getComplex(String primitive)
    {
        if (primitive == null)
            return null;

        if (primitive.startsWith("#"))
        {
            int hexCode = HexFormat.fromHexDigits(primitive.replace("#", ""));
            return Color.fromRGB(hexCode);
        }

        String[] colors = primitive.split(",");

        if (colors.length == 0)
            return null;

        try
        {
            int r = Integer.parseInt(colors[0]);
            int g = Integer.parseInt(colors[1]);
            int b = Integer.parseInt(colors[2]);

            return Color.fromRGB(r, g, b);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    @Override
    public ColorField fillField(ItemTemplate template, ConfigurationSection section)
    {
        return new ColorField(template, this, section.getString(this.key));
    }
}
