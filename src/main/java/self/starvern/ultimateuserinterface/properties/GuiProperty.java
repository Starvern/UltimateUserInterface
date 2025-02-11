package self.starvern.ultimateuserinterface.properties;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

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
    public Map<String, String> getPlaceholders()
    {
        return Map.of("{" + this.key + "}", this.value.toString());
    }

    /**
     * @param input The String to parse.
     * @return A String with placeholders parsed, or null if input is null.
     * @since 0.6.0
     */
    public @Nullable String parsePlaceholders(String input)
    {
        if (input == null) return null;

        String output = input;
        Map<String, String> placeholders = this.getPlaceholders();

        for (String placeholder : placeholders.keySet())
        {
            String value = placeholders.get(placeholder);
            output = output.replace(placeholder, value);
        }

        return output;
    }

    @Override
    public String toString() {
        return "{" + this.getKey() + "}";
    }
}
