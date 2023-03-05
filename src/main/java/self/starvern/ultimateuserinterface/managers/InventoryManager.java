package self.starvern.ultimateuserinterface.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InventoryManager
{
    private static final Map<UUID, Inventory> inventories = new HashMap<>();

    public static void saveInventory(HumanEntity human)
    {
        inventories.put(human.getUniqueId(), human.getInventory());
    }

    public static Optional<Inventory> getInventory(UUID uuid)
    {
        return Optional.of(inventories.get(uuid));
    }

    public static Optional<Inventory> removeInventory(UUID uuid)
    {
        return Optional.of(inventories.remove(uuid));
    }

    /**
     * Restores all the inventories saved, clearing the map.
     */
    public static void restoreInventories()
    {
        for (UUID uuid : inventories.keySet())
        {
            restoreInventory(uuid);
        }
    }

    /**
     * Restore a specific player's inventory.
     * @param uuid The player's UUID.
     */
    public static void restoreInventory(UUID uuid)
    {
        Optional<Player> player = Bukkit.getOnlinePlayers()
                .stream()
                .filter(onlinePlayer -> onlinePlayer.getUniqueId().equals(uuid))
                .map(OfflinePlayer::getPlayer)
                .findFirst();

        if (player.isEmpty())
            return;

        Optional<Inventory> inventory = getInventory(uuid);

        if (inventory.isEmpty())
            return;

        player.get().getInventory().setContents(inventory.get().getContents());
        inventories.remove(uuid);
    }
}
