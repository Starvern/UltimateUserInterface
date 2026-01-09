package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemFlagFieldType extends ItemFieldType<ItemFlag[], List<String>>
{
    public ItemFlagFieldType(UUI api)
    {
        super(api, "flags");
    }

    @Override
    public ItemFlag @NotNull [] getComplex(@Nullable List<String> primitive)
    {
        if (primitive == null)
            return new ItemFlag[0];

        List<ItemFlag> flags = new ArrayList<>();

        for (int index = 0; index < primitive.size(); index++)
        {
            String flagName = primitive.get(index);
            try
            {
                flags.add(ItemFlag.valueOf(flagName.toUpperCase(Locale.ROOT)));
            }
            catch (IllegalArgumentException ignored)
            {
                this.api.getLogger().warning("Invalid item flag: " + flagName);
            }
        }

        return flags.toArray(new ItemFlag[]{});
    }

    @Override
    public ItemField<ItemFlag[], List<String>> fillField(ItemTemplate template, ConfigurationSection section)
    {
        return new ItemFlagField(template, this, section.getStringList(this.key));
    }
}
