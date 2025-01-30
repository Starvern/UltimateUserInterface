package self.starvern.ultimateuserinterface.macros;

import javax.annotation.Nullable;

public class ActionTrigger
{
    private final ActionType type;
    private final @Nullable String id;

    public ActionTrigger(ActionType type)
    {
        this.type = type;
        this.id = null;
    }

    public ActionTrigger(ActionType type, @Nullable String id)
    {
        this.type = type;
        this.id = id;
    }

    /**
     * @return The type of event.
     * @since 0.5.0
     */
    public ActionType getType()
    {
        return this.type;
    }

    /**
     * @return The additional id of event (for GuiCustomEvent).
     * @since 0.5.0
     */
    @Nullable
    public String getId()
    {
        return this.id;
    }
}
