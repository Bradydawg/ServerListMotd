package eu.kennytv.serverlistmotd.core.util;

import java.util.UUID;

public abstract class SenderInfo {

    public abstract UUID getUuid();

    public abstract String getName();

    public abstract boolean hasPermission(String permission);

    public abstract void sendMessage(String message);
}