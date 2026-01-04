package self.starvern.ultimateuserinterface.macros;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.api.GuiCustomEvent;
import self.starvern.ultimateuserinterface.api.GuiEvent;
import self.starvern.ultimateuserinterface.lib.GuiBased;
import self.starvern.ultimateuserinterface.lib.GuiItem;
import self.starvern.ultimateuserinterface.lib.GuiPage;
import self.starvern.ultimateuserinterface.managers.ChatManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a macro from either a GuiPage or GuiItem.
 * @param <T> The holder this macro runs for, either GuiItem or GuiPage.
 * @since 0.4.2
 */
public class GuiAction<T extends GuiBased>
{
    private final T holder;
    private final Macro macro;
    private final ActionTrigger trigger;
    private final String raw;
    private final List<String> arguments;
    private final Player player;

    public GuiAction(T holder, Macro macro, ActionTrigger trigger, String raw, Player player)
    {
        this.holder = holder;
        this.macro = macro;
        this.trigger = trigger;
        this.raw = raw;
        List<String> buffer = List.of(this.raw.split(" "));
        this.arguments = new ArrayList<>(buffer.subList(1, buffer.size()));
        this.player = player;
    }

    public T getHolder()
    {
        return this.holder;
    }

    public Macro getMacro()
    {
        return this.macro;
    }

    public ActionTrigger getTrigger()
    {
        return this.trigger;
    }

    public String getRaw()
    {
        return this.raw;
    }

    /**
     * @return The arguments parsed with property placeholders.
     * @since 0.5.0
     */
    public List<String> getArguments()
    {
        List<String> arguments = new ArrayList<>(this.arguments);

        if (this.holder instanceof GuiItem item)
            arguments = item.getItemTemplate()
                    .parseAllPlaceholders(arguments, player)
                    .stream()
                    .toList();

        if (this.holder instanceof GuiPage page)
            arguments = page.getProperties().parsePropertyPlaceholders(arguments, player)
                    .stream()
                    .map(ChatManager::decolorize)
                    .toList();

        return arguments;
    }

    /**
     * @return The arguments without placeholders parsed.
     * @since 0.5.0
     */
    public List<String> getRawArguments()
    {
        return new ArrayList<>(this.arguments);
    }

    public void setArguments(List<String> newArgs)
    {
        this.arguments.clear();
        this.arguments.addAll(newArgs);
    }

    /**
     * @param event The event to execute.
     * @since 0.4.2
     */
    public void execute(@NotNull GuiEvent event)
    {
        if (!this.trigger.getType().getEventClass().equals(event.getClass()))
            return;

        if (!(event instanceof GuiCustomEvent customEvent))
        {
            this.macro.run(event, this);
            return;
        }

        String eventId = customEvent.getId();
        String actionId = this.trigger.getId();

        if (eventId == null || actionId == null) return;

        if (eventId.equalsIgnoreCase(actionId))
            this.macro.run(event, this);
    }
}
