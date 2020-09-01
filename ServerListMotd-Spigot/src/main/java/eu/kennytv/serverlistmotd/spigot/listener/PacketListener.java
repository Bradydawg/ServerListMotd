package eu.kennytv.serverlistmotd.spigot.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import eu.kennytv.serverlistmotd.spigot.ServerListMotdSpigotPlugin;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PacketListener extends PacketAdapter implements IPingListener {
    private static final String PLAYERS_FORMAT = " §7%d§8/§7%d";
    private final Settings settings;
    private final ServerListMotdSpigotPlugin plugin;
    private WrappedServerPing.CompressedImage image;

    public PacketListener(final ServerListMotdSpigotPlugin plugin, final Settings settings) {
        super(plugin.getPlugin(), ListenerPriority.HIGH, PacketType.Status.Server.SERVER_INFO);
        this.plugin = plugin;
        this.settings = settings;
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(final PacketEvent event) {
        final WrappedServerPing ping = event.getPacket().getServerPings().read(0);
        if (settings.hasCustomPlayerCount()) {
            final String versionName = settings.showPlayerCount() ? settings.getPlayerCountMessage()
                    + String.format(PLAYERS_FORMAT, ping.getPlayersOnline(), ping.getPlayersMaximum()) : settings.getPlayerCountMessage();
            ping.setVersionName(plugin.replacePlaceholders(versionName));
            ping.setVersionProtocol(0);
        }

        ping.setMotD(plugin.replacePlaceholders(settings.getMotd()));

        if (settings.hasCustomPlayerCountHoverMessage()) {
            final List<WrappedGameProfile> players = new ArrayList<>();
            for (final String string : settings.getPlayerCountHoverMessage().split("%NEWLINE%")) {
                players.add(new WrappedGameProfile(UUID.randomUUID(), plugin.replacePlaceholders(string)));
            }
            ping.setPlayers(players);
        }

        if (image != null) {
            ping.setFavicon(image);
        }
    }

    @Override
    public boolean loadIcon() {
        try {
            final File file = new File(settings.getServerIconPath());
            if (!file.exists()) return false;
            image = WrappedServerPing.CompressedImage.fromPng(ImageIO.read(file));
        } catch (final Exception e) {
            return false;
        }
        return true;
    }
}
