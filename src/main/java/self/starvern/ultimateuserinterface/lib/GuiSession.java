package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiTickEvent;

import java.util.Set;

public class GuiSession
{
    private final UUI api;
    private final Gui gui;
    private final HumanEntity viewer;

    private final BukkitTask task;

    @NotNull
    private GuiPage page;

    protected GuiSession(UUI api, @NotNull GuiPage page, HumanEntity viewer)
    {
        this.api = api;
        this.gui = page.getGui();
        this.page = page;
        this.viewer = viewer;
        this.task = Bukkit.getScheduler().runTaskTimer(this.api.getPlugin(), () -> {
            Bukkit.getScheduler().runTask(this.api.getPlugin(), () ->
                    Bukkit.getPluginManager().callEvent(new GuiTickEvent(this.viewer, this.page)));
        }, 0, page.getTick());
    }

    /**
     * @param page The page to open.
     * @param viewer The player opening the page.
     * @return The created session.
     */
    public static GuiSession start(UUI api, @NotNull GuiPage page, HumanEntity viewer)
    {
        GuiSession session = new GuiSession(api, page, viewer);
        Set<GuiSession> sessions = page.getGui().getSessions();

        for (GuiSession activeSession : sessions)
        {
            if (activeSession.getViewer().getUniqueId().equals(viewer.getUniqueId()))
            {
                activeSession.endSession();
                break;
            }
        }

        page.getGui().getSessions().add(session);
        viewer.openInventory(page.getInventory());

        return session;
    }

    public Gui getGui()
    {
        return gui;
    }

    @NotNull
    public GuiPage getPage()
    {
        return page;
    }

    public void setPage(@NotNull GuiPage page)
    {
        this.page = page;
    }

    public HumanEntity getViewer()
    {
        return viewer;
    }

    public void endSession()
    {
        Bukkit.getScheduler().runTask(this.api.getPlugin(), () -> {
            page.reloadItems();
            gui.getSessions().remove(this);
            Bukkit.getScheduler().cancelTask(this.task.getTaskId());
        });
    }
}
