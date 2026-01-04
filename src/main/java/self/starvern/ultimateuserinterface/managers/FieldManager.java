package self.starvern.ultimateuserinterface.managers;

import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;

import java.util.HashSet;
import java.util.Set;

public class FieldManager
{
    private final Set<ItemFieldType<?, ?>> fields;

    public FieldManager()
    {
        this.fields = new HashSet<>();
    }

    public void registerFieldType(ItemFieldType<?, ?> field)
    {
        this.fields.add(field);
    }

    public @Nullable ItemFieldType<?, ?> getFieldByKey(String key)
    {
        for (ItemFieldType<?, ?> field : fields)
        {
            if (field.getKey().equals(key))
                return field;
        }
        return null;
    }
}
