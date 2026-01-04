package self.starvern.ultimateuserinterface.item.data.impl;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;
import self.starvern.ultimateuserinterface.managers.ChatManager;
import java.util.List;

public class LoreFieldType extends ItemFieldType<List<Component>, List<String>>
{
    public LoreFieldType(UUI api)
    {
        super(api, "lore");
    }

    @Override
    public @Nullable List<Component> getComplex(@Nullable List<String> primitive)
    {
        return ChatManager.colorize(primitive);
    }

    @Override
    public LoreField fillField(ItemTemplate template, ConfigurationSection section)
    {
        return new LoreField(template, this, section.getStringList(this.key));
    }
}
