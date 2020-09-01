package eu.kennytv.serverlistmotd.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Utility class to get the {@link IServerListMotd} instance for the Spigot/Bukkit version of the plugin.
 * <p>
 * This class is NOT available for the BungeeCord version!
 * </p>
 *
 * @author KennyTV
 * @since 1.0
 */
public final class ServerListMotdSpigotAPI {

    /**
     * Returns API instance of IServerListMotd.
     *
     * @return {@link IServerListMotd} instance
     */
    public static IServerListMotd getAPI() {
        final Plugin serverListMotd = Bukkit.getPluginManager().getPlugin("ServerListMotdSpigot");
        if (serverListMotd == null) {
            Bukkit.getLogger().warning("Could not get instance of ServerListMotdSpigot!");
            return null;
        }
        return ((IServerListMotdBase) serverListMotd).getApi();
    }
}
