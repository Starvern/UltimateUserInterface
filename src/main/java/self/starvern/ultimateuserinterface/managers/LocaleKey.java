package self.starvern.ultimateuserinterface.managers;

import java.util.Locale;

public enum LocaleKey
{
    PLUGIN_RELOAD,
    PLUGIN_INFO,
    NO_PERMISSION,
    NO_PLAYER,
    GUI_INVALID,
    GUI_UNKNOWN,
    ARGUMENT_MISSING,
    ARGUMENT_INVALID;

    public String asPath()
    {
        return this.toString().toLowerCase(Locale.ROOT);
    }
}
