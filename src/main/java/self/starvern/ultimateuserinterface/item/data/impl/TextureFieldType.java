package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;

public class TextureFieldType extends ItemFieldType<String, String>
{
    public TextureFieldType(UUI api)
    {
        super(api, "texture");
    }

    @Override
    public @Nullable String getComplex(@Nullable String primitive)
    {
        return primitive;
    }

    @Override
    public ItemField<String, String> fillField(ItemTemplate template, ConfigurationSection section)
    {
        return new TextureField(template, this, section.getString(this.key));
    }
}
