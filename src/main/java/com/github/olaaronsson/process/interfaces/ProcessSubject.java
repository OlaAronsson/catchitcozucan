package com.github.olaaronsson.process.interfaces;

import java.io.Serializable;

import com.github.olaaronsson.process.histogram.LifeCycleProvider;

public interface ProcessSubject extends Serializable, LifeCycleProvider {
    Integer id();
    String subjectIdentifier();
    int getErrorCode();
}
