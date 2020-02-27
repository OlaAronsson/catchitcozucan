package com.github.olaaronsson.process.interfaces;

public interface Job {
	String name();
	void doJob();
	ProcessSubject provideSubjectSample();
	boolean isExecuting();
}
