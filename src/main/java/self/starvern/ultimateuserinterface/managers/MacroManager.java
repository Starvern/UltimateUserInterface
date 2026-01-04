package self.starvern.ultimateuserinterface.managers;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.macros.Macro;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class MacroManager
{
    private final Set<Macro> macros = new HashSet<>();

    /**
     * @param macro The macro to register with the API.
     * @since 0.4.0
     */
    public void register(Macro macro)
    {
        this.macros.add(macro);
    }

    /**
     * Unregister a {@link Macro} from the API.
     * @param macro The {@link Macro} to unregister.
     * @since 0.4.0
     */
    public void unregister(Macro macro)
    {
        this.macros.remove(macro);
    }

    /**
     * @param action The action to search by.
     * @return The Macro with the given action, or null.
     */
    public @Nullable Macro getMacro(String action)
    {
        String lowerAction = action.toLowerCase(Locale.ROOT);
        for (Macro macro : this.macros)
        {
            if (lowerAction.startsWith(macro.toString().toLowerCase(Locale.ROOT)))
                return macro;
        }
        return null;
    }

    /**
     * @param plugin The plugin to search by.
     * @return All {@link Macro}s registered under the given {@link Plugin}.
     * @since 0.4.0
     */
    public Set<Macro> getMacros(Plugin plugin)
    {
        return this.macros.stream()
                .filter(macro -> macro.getPlugin().equals(plugin))
                .collect(Collectors.toSet());
    }

    /**
     * @return All {@link Macro}s registered with the API.
     * @since 0.4.0
     */
    public Set<Macro> getMacros()
    {
        return this.macros;
    }
}
