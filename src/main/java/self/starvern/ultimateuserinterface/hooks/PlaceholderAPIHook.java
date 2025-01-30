package self.starvern.ultimateuserinterface.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PlaceholderAPIHook
{
    /**
     * @return If PlaceholderAPI is installed.
     * @since 0.1.0
     */
    public static boolean isInstalled()
    {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    /**
     * Parses nested PlaceholderAPI Placeholders up to 5 times.
     * @param player The player to parse the placeholders.
     * @param text The text to check for placeholders.
     * @return The parsed text.
     * @since 0.1.0
     */
    public static String parse(Player player, String text)
    {
        if (!isInstalled() || text == null || player == null) return text;
        String output = PlaceholderAPI.setPlaceholders(player, text);
        int index = 0;
        while (PlaceholderAPI.containsPlaceholders(output) && index < 5)
        {
            output = PlaceholderAPI.setPlaceholders(player, text);
            index ++;
        }
        return output;
    }

    /**
     * Parses nested PlaceholderAPI Placeholders up to 5 times.
     * @param player The player to parse the placeholders.
     * @param list The texts to check for placeholders.
     * @return The parsed text.
     * @since 0.1.0
     */
    public static List<String> parse(Player player, List<String> list)
    {
        if (!isInstalled() || list == null || player == null) return list;
        return list.stream()
                .map(line -> parse(player, line))
                .collect(Collectors.toList());
    }

    /**
     * Parses nested PlaceholderAPI Placeholders up to 5 times.
     * @param player The player to parse the placeholders.
     * @param text The text to check for placeholders.
     * @return The parsed text.
     * @since 0.5.0
     */
    public static String parse(OfflinePlayer player, String text)
    {
        if (!isInstalled() || text == null || player == null) return text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    /**
     * @param text The text to check.
     * @return True if the text contains Papi placeholders.
     * @since 0.5.0
     */
    public static boolean containsPlaceholders(String text)
    {
        if (!isInstalled() || text == null) return false;
        return PlaceholderAPI.containsPlaceholders(text);
    }

    /**
     * @param texts The text to check.
     * @return True if the text contains Papi placeholders.
     * @since 0.5.0
     */
    public static boolean containsPlaceholders(List<String> texts)
    {
        if (!isInstalled() || texts == null) return false;
        for (String text : texts)
        {
            if (containsPlaceholders(text))
                return true;
        }
        return false;
    }


    /**
     * Parses nested PlaceholderAPI Placeholders up to 5 times.
     * @param player The player to parse the placeholders.
     * @param list The texts to check for placeholders.
     * @return The parsed text.
     * @since 0.5.0
     */
    public static List<String> parse(OfflinePlayer player, List<String> list)
    {
        if (!isInstalled() || list == null || player == null) return list;
        return list.stream()
                .map(line -> parse(player, line))
                .collect(Collectors.toList());
    }
}
