package self.starvern.ultimateuserinterface.properties;

public class GuiProperty<T>
{
    private final String key;
    private T value;

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

    @Override
    public String toString() {
        return "{" + this.getKey() + "}";
    }
}
