package eu.kennytv.serverlistmotd.api;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Utility class to get the {@link IServerListMotd} instance for the BungeeCord version of the plugin.
 * <p>
 * This class is NOT available for the Spigot version!
 * </p>
 *
 * @author KennyTV
 * @since 1.0
 */
public final class ServerListMotdBungeeAPI {

    /**
     * Returns API instance of IServerListMotd.
     *
     * @return {@link IServerListMotd} instance
     */
    public static IServerListMotd getAPI() {
        final Plugin serverListMotd = ProxyServer.getInstance().getPluginManager().getPlugin("ServerListMotdBungee");
        if (serverListMotd == null) {
            ProxyServer.getInstance().getLogger().warning("Could not get instance of ServerListMotdBungee!");
            return null;
        }
        return ((IServerListMotdBase) serverListMotd).getApi();
    }
}
