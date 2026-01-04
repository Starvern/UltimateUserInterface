package self.starvern.ultimateuserinterface.item.data;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.item.ItemTemplate;

import java.util.function.Function;

public abstract class ItemFieldType<T, P>
{
    protected final UUI api;
    protected final String key;

    public ItemFieldType(UUI api, String key)
    {
        this.api = api;
        this.key = key;
    }

    /**
     * @return The key / path this field uses.
     * @since 0.7.0
     */
    public String getKey()
    {
        return this.key;
    }

    /**
     * Register this type with the api.
     * @since 0.7.0
     */
    public void register()
    {
        this.api.getFieldManager().registerFieldType(this);
    }

    /**
     * @param primitive The value from the config.
     * @return The parsed value as {@link T}
     * @since 0.7.0
     */
    public @Nullable T getComplex(@Nullable P primitive)
    {
        return null;
    }

    /**
     * @param primitive The value from the config.
     * @return The parsed value as {@link T}
     * @since 0.7.0
     */
    public @Nullable T getComplex(@Nullable P primitive, Function<P, P> preprocess)
    {
        return this.getComplex(preprocess.apply(primitive));
    }

    /**
     * @param template The {@link ItemTemplate} for this field.
     * @param section The section to pull from.
     * @return A new {@link ItemField}.
     * @since 0.7.0
     */
    abstract public ItemField<T, P> fillField(ItemTemplate template, ConfigurationSection section);
}
