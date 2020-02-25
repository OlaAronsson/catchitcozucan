package thehole.black.process.histogram;

public interface LifeCycleProvider {
	Enum[] getCycle();
	Enum getCurrentStatus();
	String getCurrentProcess();
}