package self.starvern.ultimateuserinterface.properties;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.hooks.PlaceholderAPIHook;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.managers.ChatManager;
import self.starvern.ultimateuserinterface.properties.impl.DoubleProperty;
import self.starvern.ultimateuserinterface.properties.impl.IntegerProperty;
import self.starvern.ultimateuserinterface.properties.impl.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GuiProperties<T extends GuiBased>
{
    private final T holder;
    private final List<GuiProperty<?>> properties;

    public GuiProperties(T holder)
    {
        this.holder = holder;
        this.properties = new ArrayList<>();
    }

    /**
     * @return The properties of the holder.
     * @since 0.5.0
     */
    public List<GuiProperty<?>> getProperties()
    {
        return this.properties;
    }

    /**
     * Copies all properties.
     * @param properties The properties to add.
     * @param clear True, to clear existing properties.
     * @since 0.6.2
     */
    public void copy(GuiProperties<T> properties, boolean clear)
    {
        if (clear)
            this.properties.clear();
        this.properties.addAll(properties.getProperties());
    }

    /**
     * Loads properties from config.
     * @since 0.5.0
     */
    public void loadProperties()
    {
        this.properties.clear();
        ConfigurationSection section = holder.getSection().getConfigurationSection("properties");

        if (section == null) return;

        for (Map.Entry<String, Object> entry : section.getValues(false).entrySet())
        {
            if (entry.getValue() instanceof String)
                this.setProperty(new StringProperty(entry.getKey(), (String) entry.getValue()), true);
            if (entry.getValue() instanceof Integer)
                this.setProperty(new IntegerProperty(entry.getKey(), (Integer) entry.getValue()), true);
            if (entry.getValue() instanceof Double)
                this.setProperty(new DoubleProperty(entry.getKey(), (Double) entry.getValue()), true);
        }
    }

    /**
     * Sets a property
     * @param property The property to set.
     * @param override Whether to override any existing properties with an identical key
     * @since 0.5.0
     */
    public void setProperty(GuiProperty<?> property, boolean override)
    {
        GuiProperty<?> oldProperty = null;

        for (GuiProperty<?> existingProperty : this.properties)
        {
            if (!existingProperty.getKey().equalsIgnoreCase(property.getKey()))
                continue;

            if (!override)
                return;

            oldProperty = existingProperty;
        }

        if (oldProperty != null)
            this.properties.remove(oldProperty);

        this.properties.add(property);
    }

    /**
     * Removes a property with the given key.
     * @param key The key to check.
     * @since 0.6.2
     */
    public void removeProperty(String key)
    {
        for (GuiProperty<?> property : this.properties)
        {
            if (property.getKey().equalsIgnoreCase(key))
            {
                this.properties.remove(property);
                return;
            }
        }
    }

    /**
     * @param input The text to check.
     * @return If the text contains property placeholders.
     * @since 0.5.0
     */
    public boolean containsPlaceholders(String input)
    {
        if (input == null) return false;
        return Pattern.compile("<(.*?)>", Pattern.CASE_INSENSITIVE)
                .matcher(input)
                .find();
    }

    /**
     * @param input The string to parse.
     * @return The parsed {@link Component}
     * @since 0.5.0
     */
    public Component parsePropertyPlaceholders(@NotNull String input, OfflinePlayer player)
    {
        List<TagResolver.Single> placeholdersList = new ArrayList<>();

        for (GuiProperty<?> property : this.properties)
            placeholdersList.addAll(property.getPlaceholders(player));

        TagResolver.Single[] placeholders = placeholdersList.toArray(new TagResolver.Single[]{});

        String output = input;
        int i = 0;

        while (containsPlaceholders(output) && i < 5)
        {
            i++;
            output = ChatManager.decolorize(ChatManager.colorize(output, placeholders));
        }

        return ChatManager.colorize(PlaceholderAPIHook.parse(player, output), placeholders);
    }

    /**
     * @param inputs The strings to parse.
     * @return The parsed {@link Component}
     * @since 0.5.0
     */
    public List<Component> parsePropertyPlaceholders(@NotNull List<String> inputs, OfflinePlayer player)
    {
        List<Component> outputs = new ArrayList<>();
        for (String input : inputs)
            outputs.add(parsePropertyPlaceholders(input, player));

        return outputs;
    }
}
