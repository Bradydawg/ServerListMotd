package eu.kennytv.serverlistmotd.spigot;

import eu.kennytv.serverlistmotd.api.IServerListMotd;
import eu.kennytv.serverlistmotd.api.IServerListMotdBase;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ServerListMotdSpigotBase extends JavaPlugin implements IServerListMotdBase {
    private IServerListMotd serverListMotd;

    @Override
    public void onEnable() {
        serverListMotd = new ServerListMotdSpigotPlugin(this);
    }

    File getPluginFile() {
        return getFile();
    }

    @Override
    public IServerListMotd getApi() {
        return serverListMotd;
    }
}
