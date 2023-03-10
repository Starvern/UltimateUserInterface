package self.starvern.ultimateuserinterface.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Optional;

public class PlayerUtility
{
    /**
     * @param name The name of the player.
     * @return An Optional of the player (offline or online).
     * @since 0.4.0
     */
    public static Optional<OfflinePlayer> getPlayer(String name)
    {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .filter(offlinePlayer -> {
                    String playerName = offlinePlayer.getName();
                    if (playerName == null) return false;
                    return playerName.equalsIgnoreCase(name);
                })
                .findFirst();
    }
}
