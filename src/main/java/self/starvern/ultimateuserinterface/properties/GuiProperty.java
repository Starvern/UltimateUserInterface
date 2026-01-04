package self.starvern.ultimateuserinterface.properties;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;

import java.util.List;

public class GuiProperty<T>
{
    protected final String key;
    protected T value;

    public GuiProperty(String key, T value)
    {
        this.key = key;
        this.value = value;
    }

    /**
     * @return The key associated with the value.
     * @since 0.5.0
     */
    public String getKey()
    {
        return this.key;
    }

    /**
     * @return The value held within the key.
     * @since 0.5.0
     */
    public T getValue()
    {
        return this.value;
    }

    /**
     * @param value The value to set.
     * @since 0.5.0
     */
    public void setValue(T value)
    {
        this.value = value;
    }

    /**
     * @return All placeholders this property can provide. {key} by default.
     * @since 0.6.0
     */
    public List<TagResolver.Single> getPlaceholders(OfflinePlayer player)
    {
        String value = PlaceholderAPIHook.parse(player, this.value.toString());
        return List.of(Placeholder.parsed(this.key, value));
    }

    @Override
    public String toString()
    {
        return this.getKey();
    }
}
