package self.starvern.ultimateuserinterface.managers;

import com.mojang.brigadier.Message;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class LocaleEntry
{
    private final ConfigurationSection section;
    private final LocaleKey key;
    private final List<TagResolver> placeholders;

    public LocaleEntry(ConfigurationSection section, LocaleKey key)
    {
        this.section = section;
        this.key = key;
        this.placeholders = new ArrayList<>();
    }

    public LocaleEntry supplyPlaceholder(TagResolver placeholder)
    {
        this.placeholders.add(placeholder);
        return this;
    }

    public ConfigurationSection getSection()
    {
        return this.section;
    }

    public LocaleKey getKey()
    {
        return this.key;
    }

    public String getValue()
    {
        return this.section.getString(this.key.asPath());
    }

    public Component getComponent()
    {
        return ChatManager.colorize(this.getValue(), this.placeholders.toArray(new TagResolver[]{}));
    }

    public Message getMessage()
    {
        return MessageComponentSerializer.message().serialize(this.getComponent());
    }
}
