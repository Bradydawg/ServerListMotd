package eu.kennytv.serverlistmotd.bungee;

import eu.kennytv.serverlistmotd.api.IServerListMotd;
import eu.kennytv.serverlistmotd.api.IServerListMotdBase;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public final class ServerListMotdBungeeBase extends Plugin implements IServerListMotdBase {
    private IServerListMotd serverListMotd;

    @Override
    public void onEnable() {
        serverListMotd = new ServerListMotdBungeePlugin(this);
    }

    @Override
    public IServerListMotd getApi() {
        return serverListMotd;
    }

    File getPluginFile() {
        return getFile();
    }
}
