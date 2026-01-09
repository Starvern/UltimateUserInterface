package self.starvern.ultimateuserinterface.item.data.impl;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemField;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;
import self.starvern.ultimateuserinterface.lib.ContextPriority;

public class PlayerFieldType extends ItemFieldType<OfflinePlayer, String>
{
    public PlayerFieldType(UUI api)
    {
        super(api, "player", ContextPriority.HIGHEST);
    }

    @Override
    public @Nullable OfflinePlayer getComplex(@Nullable String primitive)
    {
        if (primitive == null)
            return null;

        return Bukkit.getOfflinePlayerIfCached(primitive);
    }

    @Override
    public ItemField<OfflinePlayer, String> fillField(ItemTemplate template, ConfigurationSection section)
    {
        return new PlayerField(template, this, section.getString(this.key));
    }
}
