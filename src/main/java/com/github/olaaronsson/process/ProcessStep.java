package com.github.olaaronsson.process;

public interface ProcessStep {
	void execute();
	String description();
	Enum<?> statusUponSuccess(); //NOSONAR IT IS ENUMS WE PLAY...!
	Enum<?> statusUponFailure(); //NOSONAR IT IS ENUMS WE PLAY...!
	String processName();
}
