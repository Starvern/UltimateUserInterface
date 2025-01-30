package self.starvern.ultimateuserinterface.api;

import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.lib.GuiPage;

/**
 * Triggered by CustomEventMacro, used to configure specific event handling.
 * @since 0.5.0
 */
public class GuiCustomEvent extends GuiEvent
{
    private final String id;

    public GuiCustomEvent(@NotNull HumanEntity human, @NotNull GuiPage page, @NotNull String id)
    {
        super(human, page);
        this.id = id;
    }

    /**
     * @return The ID of this custom event.
     * @since 0.1.0
     */
    public String getId()
    {
        return id;
    }
}
