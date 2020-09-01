package eu.kennytv.serverlistmotd.spigot.listener;

import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import eu.kennytv.serverlistmotd.spigot.ServerListMotdSpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.io.File;

public final class ServerListPingListener implements Listener, IPingListener {
    private final ServerListMotdSpigotPlugin plugin;
    private final Settings settings;
    private CachedServerIcon serverIcon;

    public ServerListPingListener(final ServerListMotdSpigotPlugin plugin, final Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void serverListPing(final ServerListPingEvent event) {
        event.setMotd(plugin.replacePlaceholders(settings.getMotd()));
        if (serverIcon != null) {
            event.setServerIcon(serverIcon);
        }
    }

    @Override
    public boolean loadIcon() {
        try {
            final File file = new File(settings.getServerIconPath());
            if (!file.exists()) return false;
            serverIcon = Bukkit.loadServerIcon(ImageIO.read(file));
        } catch (final Exception e) {
            return false;
        }
        return true;
    }
}
