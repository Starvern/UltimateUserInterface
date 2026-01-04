package self.starvern.ultimateuserinterface.item.data.impl;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;
import self.starvern.ultimateuserinterface.managers.ChatManager;

public class CustomNameFieldType extends ItemFieldType<Component, String>
{
    public CustomNameFieldType(UUI api)
    {
        super(api, "name");
    }

    @Override
    public @Nullable Component getComplex(String primitive)
    {
        return ChatManager.colorize(primitive);
    }

    @Override
    public CustomNameField fillField(ItemTemplate template, ConfigurationSection section)
    {
        return new CustomNameField(template, this, section.getString(this.key));
    }
}
