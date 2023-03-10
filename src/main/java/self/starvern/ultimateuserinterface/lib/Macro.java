package self.starvern.ultimateuserinterface.lib;

import java.util.List;

public interface Macro
{
    /**
     * <p>
     *     The identifier of a macro is found in the item action:
     *     [PluginName:Identifier]
     * </p>
     * @return The identifier of the macro.
     */
    String getIdentifier();

    /**
     * <p>
     *     Executes the macro, with the given arguments.
     *
     *     Macro arguments are defined as such:
     *
     *     [PluginName:Identifier] argument1 argument2 argument3
     * </p>
     */
    void execute(List<String> arguments);
}
