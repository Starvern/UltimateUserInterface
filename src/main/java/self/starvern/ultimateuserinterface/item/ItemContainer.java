package self.starvern.ultimateuserinterface.item;

import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ItemContainer implements PersistentDataType<byte[], ItemConfig>
{
    /**
     * Returns the primitive data type of this tag.
     *
     * @return the class
     */
    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType()
    {
        return byte[].class;
    }

    /**
     * Returns the complex object type the primitive value resembles.
     *
     * @return the class type
     */
    @NotNull
    @Override
    public Class<ItemConfig> getComplexType()
    {
        return ItemConfig.class;
    }

    /**
     * Returns the primitive data that resembles the complex object passed to
     * this method.
     *
     * @param complex the complex object instance
     * @param context the context this operation is running in
     * @return the primitive value
     */
    @Override
    public byte @NotNull [] toPrimitive(@NotNull ItemConfig complex, @NotNull PersistentDataAdapterContext context)
    {
        return SerializationUtils.serialize(complex);
    }

    /**
     * Creates a complex object based of the passed primitive value
     *
     * @param primitive the primitive value
     * @param context the context this operation is running in
     * @return the complex object instance
     */
    @NotNull
    @Override
    public ItemConfig fromPrimitive(byte[] primitive, @NotNull PersistentDataAdapterContext context)
    {
        try (InputStream inputStream = new ByteArrayInputStream(primitive);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream))
        {
            return (ItemConfig) objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException exception)
        {
            exception.printStackTrace();
        }

        return new ItemConfig();
    }
}
