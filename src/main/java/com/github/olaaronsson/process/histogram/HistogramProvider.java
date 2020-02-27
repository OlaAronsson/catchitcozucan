package com.github.olaaronsson.process.histogram;

import java.util.stream.Stream;

import com.github.olaaronsson.process.interfaces.ProcessSubject;

public interface HistogramProvider {
	HistogramStatus getHistogram(Stream<ProcessSubject> subjectStream);
	HistogramStatus makeSampleHistogram(Integer[] data);
}
