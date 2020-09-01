package eu.kennytv.serverlistmotd.spigot.command;

import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.command.ServerListMotdCommand;
import eu.kennytv.serverlistmotd.core.util.SenderInfo;
import eu.kennytv.serverlistmotd.spigot.ServerListMotdSpigotPlugin;
import eu.kennytv.serverlistmotd.spigot.util.BukkitSenderInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class ServerListMotdSpigotCommand extends ServerListMotdCommand implements CommandExecutor {

    public ServerListMotdSpigotCommand(final ServerListMotdSpigotPlugin plugin, final Settings settings) {
        super(plugin, settings, "ServerListMotdSpigot");
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String s, final String[] args) {
        execute(new BukkitSenderInfo(sender), args);
        return true;
    }

    @Override
    protected void checkForUpdate(final SenderInfo sender) {
        if (plugin.updateAvailable()) {
            sender.sendMessage(plugin.getPrefix() + "§cNewest version available: §aVersion " + plugin.getNewestVersion() + "§c, you're still on §a" + plugin.getVersion());
            sender.sendMessage(plugin.getPrefix() + "§c§lWARNING: §cYou will have to restart the server to prevent further issues and to complete the update!" +
                    " If you can't do that, don't update!");
            sender.sendMessage(plugin.getPrefix() + "§eUse §c§l/serverlistmotdspigot forceupdate §eto update!");
        } else
            sender.sendMessage(plugin.getPrefix() + "§aYou already have the latest version of the plugin!");
    }
}
