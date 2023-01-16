package self.starvern.ultimateuserinterface.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager
{
    /**
     * Pulls the configuration from the file.
     * @param file The file to pull from
     * @return The configuration
     */
    public static FileConfiguration getConfig(File file)
    {
        return YamlConfiguration.loadConfiguration(file);
    }
}
