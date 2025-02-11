package self.starvern.ultimateuserinterface.managers;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;

import java.lang.reflect.Field;

public class CommandManager
{
    private SimpleCommandMap simpleCommandMap;

    public CommandManager(UUI api)
    {
        this.simpleCommandMap = null;
        try
        {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            this.simpleCommandMap = (SimpleCommandMap) field.get(api.getPlugin().getServer().getPluginManager());
        }
        catch (NoSuchFieldException | IllegalAccessException exception)
        {
            api.getLogger().warning("Failed to setup command map for aliases.");
        }
    }

    public @Nullable SimpleCommandMap getCommandMap()
    {
        return this.simpleCommandMap;
    }
}
