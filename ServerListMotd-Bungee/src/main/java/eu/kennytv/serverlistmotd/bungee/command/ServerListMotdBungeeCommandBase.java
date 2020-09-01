package eu.kennytv.serverlistmotd.bungee.command;

import eu.kennytv.serverlistmotd.bungee.util.ProxiedSenderInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public final class ServerListMotdBungeeCommandBase extends Command {
    private final ServerListMotdBungeeCommand command;

    public ServerListMotdBungeeCommandBase(final ServerListMotdBungeeCommand command) {
        super("serverlistmotd", "", "motd", "serverlistmotdbungee");
        this.command = command;
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        command.execute(new ProxiedSenderInfo(sender), args);
    }
}
