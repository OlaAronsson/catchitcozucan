package com.github.olaaronsson.process.histogram;

public interface LifeCycleProvider {
	Enum[] getCycle();
	Enum getCurrentStatus();
	String getCurrentProcess();
}