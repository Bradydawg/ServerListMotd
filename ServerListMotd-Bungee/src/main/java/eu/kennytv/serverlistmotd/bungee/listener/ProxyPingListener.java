package eu.kennytv.serverlistmotd.bungee.listener;

import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public final class ProxyPingListener implements Listener, IPingListener {
    private static final String PLAYERS_FORMAT = " §7%d§8/§7%d";
    private final Settings settings;
    private Favicon favicon;

    public ProxyPingListener(final Settings settings) {
        this.settings = settings;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void proxyPing(final ProxyPingEvent event) {
        final ServerPing ping = event.getResponse();
        if (settings.hasCustomPlayerCount()) {
            ping.getVersion().setProtocol(1);
            ping.getVersion().setName(settings.showPlayerCount() ? settings.getPlayerCountMessage()
                    + String.format(PLAYERS_FORMAT, ping.getPlayers().getOnline(), ping.getPlayers().getMax()) : settings.getPlayerCountMessage());
        }
        if (settings.hasCustomPlayerCountHoverMessage()) {
            ping.getPlayers().setSample(new ServerPing.PlayerInfo[]{
                    new ServerPing.PlayerInfo(settings.getPlayerCountHoverMessage().replace("%NEWLINE%", "\n"), "")
            });
        }
        ping.setDescriptionComponent(new TextComponent(TextComponent.fromLegacyText(settings.getMotd())));
        if (favicon != null) {
            ping.setFavicon(favicon);
        }
    }

    @Override
    public boolean loadIcon() {
        try {
            final File file = new File(settings.getServerIconPath());
            if (!file.exists()) return false;
            favicon = Favicon.create(ImageIO.read(file));
        } catch (final IOException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
