package thehole.black.process.histogram;

import java.util.stream.Stream;

import thehole.black.process.interfaces.ProcessSubject;

public interface HistogramProvider {
	HistogramStatus getHistogram(Stream<ProcessSubject> subjectStream);
	HistogramStatus makeSampleHistogram(Integer[] data);
}
