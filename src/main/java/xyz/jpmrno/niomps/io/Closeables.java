package xyz.jpmrno.niomps.io;

import java.io.Closeable;
import java.io.IOException;

public class Closeables {
    // TODO: private static final Logger logger = LoggerFactory.getLogger(Closeables.class);

    public static void closeSilently(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException exception) {
            // TODO: logger.error("Can't properly close resource", exception);
        }
    }

    public static void closeSilently(final Closeable closeable, String message) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException exception) {
            // TODO: logger.error(message, exception);
        }
    }
}
