package self.starvern.ultimateuserinterface.hooks;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import self.starvern.ultimateuserinterface.UUI;

import java.util.Optional;

public class HeadDatabaseHook implements Listener
{
    private static HeadDatabaseAPI api;

    public HeadDatabaseHook(UUI api)
    {
        Bukkit.getPluginManager().registerEvents(this, api.getPlugin());
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent e)
    {
        api = new HeadDatabaseAPI();
    }

    /**
     * @return An instance of HeadDatabaseAPI
     */
    public static Optional<HeadDatabaseAPI> getApi()
    {
        if (api == null)
            return Optional.empty();
        return Optional.of(api);
    }

    public static boolean isEnabled()
    {
        return getApi().isPresent();
    }
}
