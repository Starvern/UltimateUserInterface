package self.starvern.ultimateuserinterface.item.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import self.starvern.ultimateuserinterface.item.ItemTemplate;

public class ItemField<T, P>
{
    protected final ItemTemplate template;
    protected final ItemFieldType<T, P> fieldType;
    protected final P primitive;

    public ItemField(ItemTemplate template, ItemFieldType<T, P> fieldType, P primitive)
    {
        this.template = template;
        this.fieldType = fieldType;
        this.primitive = primitive;
    }

    /**
     * @return The template this field is for.
     * @since 0.7.0
     */
    public ItemTemplate getTemplate()
    {
        return this.template;
    }

    /**
     * @return The value at the given field.
     * @since 0.7.0
     */
    public P getPrimitive()
    {
        return this.primitive;
    }

    /**
     * @return The {@link ItemFieldType} associated with the primitive value.
     * @since 0.7.0
     */
    public ItemFieldType<T, P> getFieldType()
    {
        return this.fieldType;
    }

    /**
     * @return The updated {@link ItemStack} after applying this field.
     * @since 0.7.0
     */
    public ItemStack apply(ItemStack itemStack, OfflinePlayer player)
    {
        return itemStack;
    }
}
