package self.starvern.ultimateuserinterface.item;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.Serializable;

public class SerializableSection implements Serializable
{
    private final File file;
    private final String path;

    public SerializableSection(File file, String path)
    {
        this.file = file;
        this.path = path;
    }

    public ConfigurationSection getSection()
    {
        if (file == null || path == null) return new YamlConfiguration();
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        return configuration.getConfigurationSection(path);
    }
}
