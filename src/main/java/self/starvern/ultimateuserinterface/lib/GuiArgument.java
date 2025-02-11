package self.starvern.ultimateuserinterface.lib;

import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.properties.GuiProperty;
import self.starvern.ultimateuserinterface.properties.impl.DoubleProperty;
import self.starvern.ultimateuserinterface.properties.impl.IntegerProperty;
import self.starvern.ultimateuserinterface.properties.impl.StringProperty;

public class GuiArgument
{
    private final String id;
    private final String type;
    private final String defaultValue;
    private final boolean required;

    private @Nullable String value;

    public GuiArgument(String id, String type)
    {
        this.id = id;
        this.type = type;
        this.defaultValue = "";
        this.required = true;
        this.value = null;
    }

    public GuiArgument(String id, String type, String defaultValue, boolean required)
    {
        this.id = id;
        this.type = type;
        this.defaultValue = defaultValue;
        this.required = required;
        this.value = null;
    }

    /**
     * @return The id of the argument, or the key of its property.
     * @since 0.6.0
     */
    public String getId()
    {
        return id;
    }

    /**
     * @return The type of its value.
     * @since 0.6.0
     */
    public String getType()
    {
        return type;
    }

    /**
     * @return The default value if not specified (if !required).
     * @since 0.6.0
     */
    public String getDefaultValue()
    {
        return this.defaultValue;
    }

    /**
     * @return If the argument is required.
     * @since 0.6.0
     */
    public boolean isRequired()
    {
        return this.required;
    }

    /**
     * @return The value passed to this argument, or null.
     * @since 0.6.0
     */
    public @Nullable String getValue()
    {
        return this.value;
    }

    /**
     * @param value The value to set.
     * @since 0.6.0
     */
    public void setValue(@Nullable String value)
    {
        this.value = value;
    }

    /**
     * The value of this property will be the default value if this.value == null.
     * @return The argument as a property.
     * @throws NumberFormatException If the argument type is 'int'/'integer'/'double' and the value passed is invalid.
     * @since 0.6.0
     */
    public GuiProperty<?> asProperty() throws NumberFormatException
    {
        if (this.type.equalsIgnoreCase("string"))
            return new StringProperty(this.id, (this.value != null) ? this.value : defaultValue);
        if (this.type.equalsIgnoreCase("int") || this.type.equalsIgnoreCase("integer"))
        {
            int value = Integer.parseInt(this.value != null ? this.value : defaultValue);
            return new IntegerProperty(this.id, value);
        }
        if (this.type.equalsIgnoreCase("double"))
        {
            double value = Double.parseDouble(this.value != null ? this.value : defaultValue);
            return new DoubleProperty(this.id, value);
        }

        return new GuiProperty<>(this.id, (this.value != null) ? this.value : defaultValue);
    }
}
