package thehole.black.process.internal.util.io;

import ch.qos.logback.classic.Level;

public interface LoggingService extends Haltable {
	void setRootLogLevel(Level level);
}
