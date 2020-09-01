package eu.kennytv.serverlistmotd.spigot.listener;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import eu.kennytv.serverlistmotd.spigot.ServerListMotdSpigotPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;
import java.util.UUID;

public final class PaperServerListPingListener implements Listener, IPingListener {
    private static final String PLAYERS_FORMAT = " §7%d§8/§7%d";
    private final ServerListMotdSpigotPlugin plugin;
    private final Settings settings;
    private CachedServerIcon serverIcon;

    public PaperServerListPingListener(final ServerListMotdSpigotPlugin plugin, final Settings settings) {
        this.settings = settings;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void serverListPing(final PaperServerListPingEvent event) {
        event.setMotd(plugin.replacePlaceholders(settings.getMotd()));

        if (settings.hasCustomPlayerCount()) {
            event.setProtocolVersion(-1);
            final String versionName = settings.showPlayerCount() ? settings.getPlayerCountMessage()
                    + String.format(PLAYERS_FORMAT, event.getNumPlayers(), event.getMaxPlayers()) : settings.getPlayerCountMessage();
            event.setVersion(plugin.replacePlaceholders(versionName));
        }

        if (settings.hasCustomPlayerCountHoverMessage()) {
            final List<PlayerProfile> sample = event.getPlayerSample();
            sample.clear();
            for (final String string : settings.getPlayerCountHoverMessage().split("%NEWLINE%")) {
                sample.add(plugin.getServer().createProfile(UUID.randomUUID(), plugin.replacePlaceholders(string)));
            }
        }

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
