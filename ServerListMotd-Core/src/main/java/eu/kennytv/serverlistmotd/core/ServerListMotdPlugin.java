package eu.kennytv.serverlistmotd.core;

import eu.kennytv.serverlistmotd.api.IServerListMotd;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public abstract class ServerListMotdPlugin implements IServerListMotd {
    protected final String version;
    private final String prefix;
    private String newestVersion;

    protected ServerListMotdPlugin(final String prefix, final String version) {
        this.prefix = prefix;
        this.version = version;
    }

    public abstract File getPluginFile();

    public abstract void async(Runnable runnable);

    public boolean updateAvailable() {
        try {
            final HttpURLConnection c = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=57851").openConnection();
            final String newVersion = new BufferedReader(new InputStreamReader(c.getInputStream())).readLine().replaceAll("[a-zA-Z -]", "");

            final boolean available = !newVersion.equals(version);
            if (available)
                newestVersion = newVersion;

            return available;
        } catch (final Exception ignored) {
            return false;
        }
    }

    public boolean installUpdate() {
        try {
            URL url = null;
            try {
                url = new URL("https://github.com/KennyTV/ServerListMotd/releases/download/" + newestVersion + "/ServerListMotd.jar");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }

            final URLConnection conn = url.openConnection();
            final InputStream is = new BufferedInputStream(conn.getInputStream());
            final OutputStream os = new BufferedOutputStream(new FileOutputStream("plugins/ServerListMotd.tmp"));
            final byte[] chunk = new byte[1024];
            int chunkSize;
            while ((chunkSize = is.read(chunk)) != -1) {
                os.write(chunk, 0, chunkSize);
            }
            os.close();
            final File newfile = new File("plugins/ServerListMotd.tmp");
            final long newlength = newfile.length();
            if (newlength <= 10000) {
                newfile.delete();
                return false;
            } else {
                final FileInputStream is2 = new FileInputStream(new File("plugins/ServerListMotd.tmp"));
                final OutputStream os2 = new BufferedOutputStream(new FileOutputStream(getPluginFile()));
                final byte[] chunk2 = new byte[1024];
                int chunkSize2;
                while ((chunkSize2 = is2.read(chunk2)) != -1)
                    os2.write(chunk2, 0, chunkSize2);
                is2.close();
                os2.close();

                final File tmp = new File("plugins/ServerListMotd.tmp");
                tmp.delete();
                return true;
            }
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getVersion() {
        return version;
    }

    public String getNewestVersion() {
        return newestVersion;
    }
}
