package self.starvern.ultimateuserinterface.lib;

import org.bukkit.configuration.file.FileConfiguration;

import self.starvern.ultimateuserinterface.managers.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Gui
{
    private final File file;
    private final FileConfiguration config;

    private final String id;
    private final String title;
    private final List<String> patterns;
    private final List<GuiPage> pages;

    public Gui(File file)
    {
        this.file = file;
        this.config = FileManager.getConfig(file);
        this.id = file.getName().replace(".yml", "");
        this.title = this.config.getString("title", "Gui");
        this.patterns = this.config.getStringList("patterns");
        this.pages = new ArrayList<>();

        loadPages();
    }

    /**
     * Builds List<GuiPage> from the configuration.
     * @since 0.1.0
     */
    private void loadPages()
    {
        this.pages.clear();
        for (String patternName : patterns)
        {
            List<String> pattern = this.config.getStringList(patternName);
            this.pages.add(new GuiPage(this, pattern));
        }
    }

    /**
     * @return The file this crate is found.
     * @since 0.1.0
     */
    public File getFile()
    {
        return file;
    }

    /**
     * @return The config of the GUI file.
     * @since 0.1.0
     */
    public FileConfiguration getConfig()
    {
        return config;
    }

    /**
     * @return The name of the GUI as defined in the filename
     * @since 0.1.0
     */
    public String getId() {
        return id;
    }

    /**
     * @return The title of the GUI
     * @since 0.1.0
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @return The title of the GUI
     * @since 0.1.0
     */
    public List<String> getPatterns()
    {
        return patterns;
    }

    /**
     * Gets a specific page from the GUI.
     * @param page The index of the page to get.
     * @return The GuiPage
     * @since 0.1.0
     */
    public GuiPage getPage(int page)
    {
        return (page >= pages.size()) ? pages.get(0) : pages.get(page);
    }

    /**
     * @return A list of all of the pages this GUI has.
     * @since 0.1.0
     */
    public List<GuiPage> getPages()
    {
        return pages;
    }
}

