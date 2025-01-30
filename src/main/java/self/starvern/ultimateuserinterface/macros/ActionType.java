package self.starvern.ultimateuserinterface.macros;

import self.starvern.ultimateuserinterface.api.*;

import java.util.Locale;

public enum ActionType
{
    CLICK(GuiClickEvent.class),
    DRAG(GuiDragEvent.class),
    OPEN(GuiOpenEvent.class),
    CLOSE(GuiCloseEvent.class),
    TICK(GuiTickEvent.class),
    EVENT(GuiCustomEvent.class);

    private final Class<? extends GuiEvent> eventClass;

    ActionType(Class<? extends GuiEvent> eventClass)
    {
        this.eventClass = eventClass;
    }

    public Class<? extends GuiEvent> getEventClass()
    {
        return this.eventClass;
    }

    @Override
    public String toString()
    {
        return super.toString().toLowerCase(Locale.ROOT);
    }
}
