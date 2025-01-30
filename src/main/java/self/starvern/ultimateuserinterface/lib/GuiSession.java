package self.starvern.ultimateuserinterface.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import self.starvern.ultimateuserinterface.UUI;
import self.starvern.ultimateuserinterface.api.GuiTickEvent;

import java.util.Set;

public class GuiSession
{
    private final UUI api;
    private final Gui gui;
    private final HumanEntity viewer;

    private final @Nullable BukkitTask task;

    @NotNull
    private GuiPage page;

    protected GuiSession(UUI api, @NotNull GuiPage page, HumanEntity viewer)
    {
        this.api = api;
        this.gui = page.getGui();
        this.page = page;
        this.viewer = viewer;
        if (this.page.getTick() != -1)
            this.task = Bukkit.getScheduler().runTaskTimer(this.api.getPlugin(), () ->
                Bukkit.getScheduler().runTask(this.api.getPlugin(), () ->
                Bukkit.getPluginManager().callEvent(new GuiTickEvent(this.viewer, this.page))), 0, page.getTick());
        else
            this.task = null;
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

        page.getGui().addSession(session);

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
            gui.removeSession(this);
            if (this.task != null)
                Bukkit.getScheduler().cancelTask(this.task.getTaskId());
        });
    }
}
