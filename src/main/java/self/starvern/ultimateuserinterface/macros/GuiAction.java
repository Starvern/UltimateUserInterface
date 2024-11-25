package self.starvern.ultimateuserinterface.macros;

import org.bukkit.Bukkit;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a macro from either a GuiPage or GuiItem.
 * @param <T> The holder this macro runs for, either GuiItem or GuiPage.
 * @since 0.4.2
 */
public class GuiAction<T extends GuiBased>
{
    private final T holder;
    private final Macro macro;
    private final ActionType type;
    private final String raw;
    private final List<String> arguments;

    public GuiAction(T holder, Macro macro, ActionType type, String raw)
    {
        this.holder = holder;
        this.macro = macro;
        this.type = type;
        this.raw = raw;
        this.arguments = new ArrayList<>(List.of(this.raw.replace(macro + " ", "").split(" ")));
    }

    public T getHolder()
    {
        return holder;
    }

    /**
     * Copy this action under a new holder.
     * @param <K> The type of the new holder.
     * @param newHolder The new holder of this action.
     * @return The new action.
     * @since 0.4.2
     */
    public <K extends T> GuiAction<K> copyNewHolder(K newHolder)
    {
        return new GuiAction<>(newHolder, this.macro, this.type, this.raw);
    }

    public Macro getMacro()
    {
        return macro;
    }

    public ActionType getType()
    {
        return type;
    }

    public String getRaw()
    {
        return raw;
    }

    public List<String> getArguments()
    {
        return arguments;
    }

    public void setArguments(List<String> newArgs)
    {
        this.arguments.clear();
        this.arguments.addAll(newArgs);
    }

    public void execute(GuiEvent event)
    {
        if (this.type.getEventClass().equals(event.getClass()))
            this.macro.run(event, this);
    }
}
