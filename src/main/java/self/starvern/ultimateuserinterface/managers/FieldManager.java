package self.starvern.ultimateuserinterface.managers;

import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.item.data.ItemFieldType;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * @return Every registered field type sorted by priority.
     * @since 0.7.0
     */
    public List<ItemFieldType<?, ?>> getFields()
    {
        List<ItemFieldType<?, ?>> fields = new ArrayList<>(this.fields);
        fields.sort(Comparator.comparing(ItemFieldType::getPriority));
        return fields;
    }
}
