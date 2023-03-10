package self.starvern.ultimateuserinterface.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlaceholderAPIHook
{
    /**
     * @return If PlaceholderAPI is installed.
     */
    public static boolean isEnabled()
    {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    /**
     * @param player The player to parse the placeholders.
     * @param text The text to check for placeholders.
     * @return The parsed text.
     */
    public static String parse(Player player, String text)
    {
        if (!isEnabled() || text == null) return text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static List<String> parse(Player player, List<String> list)
    {
        return list.stream()
                .map(line -> parse(player, line))
                .collect(Collectors.toList());
    }

}
