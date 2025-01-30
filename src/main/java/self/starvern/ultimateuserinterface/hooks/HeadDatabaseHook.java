package self.starvern.ultimateuserinterface.hooks;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;

public class HeadDatabaseHook implements Listener
{
    private static HeadDatabaseAPI api;
    private static final String PLUGIN_NAME = "HeadDatabase";

    /**
     * Registers the DatabaseLoadEvent from HeadDatabase.
     * @param api Instance of UUI API.
     * @since 0.5.0
     */
    public static void registerListener(UUI api)
    {
        Bukkit.getPluginManager().registerEvents(new HeadDatabaseHook(), api.getPlugin());
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent event)
    {
        api = new HeadDatabaseAPI();
    }

    /**
     * @return True, if HeadDatabase is installed.
     * @since 0.5.0
     */
    public static boolean isInstalled()
    {
        return Bukkit.getPluginManager().getPlugin(PLUGIN_NAME) != null;
    }

    /**
     * @return An instance of HeadDatabaseAPI, or null if plugin is not installed.
     * @since 0.5.0
     */
    public static @Nullable HeadDatabaseAPI getApi()
    {
        return api;
    }
}
