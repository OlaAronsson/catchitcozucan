package thehole.black.process.interfaces;

public interface Process {
	String name();
    void process();
    Enum<?>[] criteriaStates(); // NOSONAR we WORK ENUMS!
    Enum<?> finishedState(); // NOSONAR we WORK ENUMS!;
}
