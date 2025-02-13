package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.lib.SlottedGuiItem;

/**
 * Triggered by CustomEventMacro, used to configure specific event handling.
 * @since 0.5.0
 */
public class GuiCustomEvent extends GuiEvent
{
    private final String id;
    private final Type type;
    private final @Nullable SlottedGuiItem item;

    public GuiCustomEvent(
            @NotNull HumanEntity human,
            @NotNull GuiPage page,
            @NotNull String id,
            @NotNull Type type,
            @Nullable SlottedGuiItem item
    )
    {
        super(human, page);
        this.id = id;
        this.type = type;
        this.item = item;
    }

    /**
     * @return The ID of this custom event.
     * @since 0.5.0
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * @return The item running this event, or null if getType() != Type.ITEM.
     * @since 0.6.1
     */
    public @Nullable SlottedGuiItem getItem()
    {
        return this.item;
    }

    /**
     * @return The type of event, either PAGE or ITEM.
     * @since 0.6.1
     */
    public Type getType()
    {
        return this.type;
    }

    public enum Type
    {
        ITEM,
        PAGE
    }
}
