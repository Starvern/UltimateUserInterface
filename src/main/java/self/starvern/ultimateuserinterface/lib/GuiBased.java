package self.starvern.ultimateuserinterface.lib;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents an object within a Gui
 * @since 0.1.0
 */
public interface GuiBased
{
    /**
     * @return The GUI this object belongs to.
     * @since 0.4.3
     */
    Gui getGui();

    /**
     * @return The section of this object.
     * @since 0.1.0
     */
    ConfigurationSection getSection();
}
