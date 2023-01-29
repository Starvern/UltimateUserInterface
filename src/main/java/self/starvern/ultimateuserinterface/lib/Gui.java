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
    public void loadPages()
    {
        this.pages.clear();
        for (String patternName : patterns)
        {
            List<String> pattern = this.config.getStringList(patternName);

            GuiPage page = new GuiPage(this, pattern);
            page.loadItems();
            this.pages.add(page);
        }
    }

    /**
     * Duplicates a page at the given index, and adds it to the end of the page list.
     * @param index The page number.
     * @since 0.1.5
     */
    public void appendPage(int index)
    {
        this.pages.add(this.pages.get((index >= this.pages.size()) ? 0 : index).duplicate());
    }

    /**
     * @return The file this crate is found.
     * @since 0.1.0
     */
    public File getFile()
    {
        return this.file;
    }

    /**
     * @return The config of the GUI file.
     * @since 0.1.0
     */
    public FileConfiguration getConfig()
    {
        return this.config;
    }

    /**
     * @return The name of the GUI as defined in the filename
     * @since 0.1.0
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return The title of the GUI
     * @since 0.1.0
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * @return The title of the GUI
     * @since 0.1.0
     */
    public List<String> getPatterns()
    {
        return this.patterns;
    }

    /**
     * Gets a specific page from the GUI.
     * @param page The index of the page to get.
     * @return The GuiPage
     * @since 0.1.0
     */
    public GuiPage getPage(int page)
    {
        return (page >= this.pages.size()) ? this.pages.get(0) : this.pages.get(page);
    }

    /**
     * @return A list of all the pages this GUI has.
     * @since 0.1.0
     */
    public List<GuiPage> getPages()
    {
        return this.pages;
    }

    /**
     * @param page The page to get the index of.
     * @return The index of the page inside the GUI pages list.
     * @since 0.1.2
     */
    public int indexOf(GuiPage page)
    {
        return this.pages.indexOf(page);
    }

    /**
     * @param page The current page
     * @return The page that appears afterwards, or the current page if it's last
     * @since 0.1.7
     */
    public GuiPage getNextPage(GuiPage page)
    {
        try
        {
            return this.pages.get(indexOf(page)+1);
        }
        catch (IndexOutOfBoundsException exception)
        {
            return page;
        }
    }

    /**
     * @param id The character associated with the item.
     * @return Every item with this character in all the pages of the GUI.
     * @since 0.1.7
     */
    public List<GuiItem> getAllItems(String id)
    {
        List<GuiItem> items = new ArrayList<>();
        for (GuiPage page : this.pages)
        {
            items.addAll(page.getItems(id));
        }
        return items;
    }
}

