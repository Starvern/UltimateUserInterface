package self.starvern.ultimateuserinterface.managers;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemInputManager
{
    private final Map<UUID, Consumer<ItemStack>> listeningPlayers = new HashMap<>();

    /**
     * @param uuid The UUID of the human to listen for.
     * @param callback The consumer called when the item is clicked.
     * @since 0.3.5
     */
    public void listenForInput(UUID uuid, Consumer<ItemStack> callback)
    {
        if (uuid == null) return;
        listeningPlayers.put(uuid, callback);
    }

    /**
     * Used for internal events only.
     * @param uuid The UUID of the human to listen for.
     * @param item The item that was clicked.
     * @since 0.3.5
     */
    public void consumeInput(UUID uuid, ItemStack item)
    {
        if (uuid == null) return;
        if (!listeningPlayers.containsKey(uuid)) return;
        listeningPlayers.get(uuid).accept(item);
        listeningPlayers.remove(uuid);
    }

    /**
     * @param uuid The UUID of the human to stop listening for.
     * @since 0.3.5
     */
    public void cancelInput(UUID uuid)
    {
        if (uuid == null) return;
        listeningPlayers.remove(uuid);
    }

    /**
     * @param uuid The UUID of the human.
     * @return If the human is having input requested from.
     * @since 0.3.5
     */
    public boolean isListeningFor(UUID uuid)
    {
        if (uuid == null) return false;
        return listeningPlayers.containsKey(uuid);
    }
}
