package eu.kennytv.serverlistmotd.bungee.command;

import eu.kennytv.serverlistmotd.bungee.ServerListMotdBungeePlugin;
import eu.kennytv.serverlistmotd.bungee.SettingsBungee;
import eu.kennytv.serverlistmotd.bungee.util.ProxiedSenderInfo;
import eu.kennytv.serverlistmotd.core.command.ServerListMotdCommand;
import eu.kennytv.serverlistmotd.core.util.SenderInfo;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public final class ServerListMotdBungeeCommand extends ServerListMotdCommand {

    public ServerListMotdBungeeCommand(final ServerListMotdBungeePlugin plugin, final SettingsBungee settings) {
        super(plugin, settings, "ServerListMotdBungee");
    }

    @Override
    protected void checkForUpdate(final SenderInfo sender) {
        if (plugin.updateAvailable()) {
            sender.sendMessage(plugin.getPrefix() + "§cNewest version available: §aVersion " + plugin.getNewestVersion() + "§c, you're still on §a" + plugin.getVersion());
            sender.sendMessage(plugin.getPrefix() + "§c§lWARNING: §cYou will have to restart the proxy to prevent further issues and to complete the update!" +
                    " If you can't do that, don't update!");
            final TextComponent tc = new TextComponent("§6× §8[§aUpdate§8]");
            tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/serverlistmotdbungee forceupdate"));
            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aClick here to update the plugin")));
            tc.addExtra(" §8< §7Or use the command §c/serverlistmotdbungee forceupdate");

            ((ProxiedSenderInfo) sender).sendMessage(tc);
        } else
            sender.sendMessage(plugin.getPrefix() + "§aYou already have the latest version of the plugin!");
    }
}
