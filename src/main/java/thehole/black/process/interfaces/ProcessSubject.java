package thehole.black.process.interfaces;

import java.io.Serializable;

import thehole.black.process.histogram.LifeCycleProvider;

public interface ProcessSubject extends Serializable, LifeCycleProvider {
    Integer id();
    String subjectIdentifier();
    int getErrorCode();
}
