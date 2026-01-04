package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;

public class MaterialFieldType extends ItemFieldType<Material, String>
{
    public MaterialFieldType(UUI api)
    {
        super(api, "material");
    }

    @Override
    public @Nullable Material getComplex(@Nullable String primitive)
    {
        if (primitive == null)
            return null;

        return Material.matchMaterial(primitive);
    }

    @Override
    public MaterialField fillField(ItemTemplate template, ConfigurationSection section)
    {
        return new MaterialField(template, this, section.getString(this.key));
    }
}
