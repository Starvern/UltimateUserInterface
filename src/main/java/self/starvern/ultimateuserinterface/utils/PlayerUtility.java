package self.starvern.ultimateuserinterface.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

public class PlayerUtility
{
    /**
     * @param name The name of the player.
     * @return The player (offline or online) or null if unknown.
     * @since 0.4.0
     */
    public static @Nullable OfflinePlayer getPlayer(String name)
    {
        for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers())
        {
            String playerName = player.getName();
            if (playerName != null && playerName.equalsIgnoreCase(name))
                return player;
        }

        return null;
    }
}
