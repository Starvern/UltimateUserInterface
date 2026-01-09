package self.starvern.ultimateuserinterface.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import self.starvern.ultimateuserinterface.UUI;

import java.io.File;

public class LocaleManager
{
    private final UUI api;
    private final File localeFile;
    private FileConfiguration config;

    public LocaleManager(UUI api)
    {
        this.api = api;

        this.localeFile = new File(this.api.getPlugin().getDataFolder(), "locale.yml");
        if (!this.localeFile.exists())
            this.api.getPlugin().saveResource("locale.yml", false);

        this.config = YamlConfiguration.loadConfiguration(localeFile);
    }

    public void reload()
    {
        this.config = YamlConfiguration.loadConfiguration(localeFile);
    }

    public FileConfiguration getConfig()
    {
        return this.config;
    }

    public LocaleEntry getEntry(LocaleKey key)
    {
        return new LocaleEntry(this.config, key);
    }
}
