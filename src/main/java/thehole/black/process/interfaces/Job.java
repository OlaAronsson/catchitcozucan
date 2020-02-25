package thehole.black.process.interfaces;

public interface Job {
	String name();
	void doJob();
	ProcessSubject provideSubjectSample();
	boolean isExecuting();
}
