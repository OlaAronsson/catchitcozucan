package thehole.black.process.impl.source.processor;

import static thehole.black.process.impl.source.processor.DaProcessStepConstants.CHKSUM;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.CHKSUM_AND_COLON;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.CHKSUM_LEN;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.CHKSUM_ORIG;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.CHKSUM_POS;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.COMMENT_HEADER_END;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.DOT;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.EMPTY;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.NL;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.SLASH;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.info;
import static thehole.black.process.impl.source.processor.DaProcessStepConstants.warn;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import thehole.black.process.exception.ProcessRuntimeException;
import thehole.black.process.internal.util.domain.BaseDomainObject;
import thehole.black.process.internal.util.io.IO;

public class DaProcessStepSourceAppender extends BaseDomainObject {
	private StringBuilder sourceToAppend;
	private boolean hasAppended;
	private Set<ElementToWork> elementsToWork;
	private final String completeSourceOrigSource;
	private final File srcFile;
	private final String originatingClass;
	private final String originatingShort;
	private final String originatingAppendeSource;
	private String chkSumOrig = CHKSUM_ORIG;
	private String chkSum = CHKSUM_ORIG;
	private Charset sourceEncoding;
	private File javaScrFileForStatusClass;

	DaProcessStepSourceAppender(File srcFile, String originatingClass, String originatingAppendeSource, String commentHeaderStart) {
		elementsToWork = new HashSet<>();
		this.sourceEncoding = null;
		this.sourceToAppend = new StringBuilder();
		this.srcFile = srcFile;
		this.originatingClass = originatingClass;
		this.originatingShort = originatingClass.substring(originatingClass.lastIndexOf(DOT) + 1, originatingClass.length()).toUpperCase();
		this.originatingAppendeSource = IO.hasContents(originatingAppendeSource) ? removeSpaceAndNewlines(originatingAppendeSource) : EMPTY;
		if (!this.originatingAppendeSource.equals(EMPTY) && this.originatingAppendeSource.contains(CHKSUM)) {
			int chksumStart = this.originatingAppendeSource.indexOf(CHKSUM_AND_COLON) + CHKSUM_AND_COLON.length();
			chkSumOrig = this.originatingAppendeSource.substring(chksumStart, (this.originatingAppendeSource.indexOf(SLASH, chksumStart + 1)));
		}
		info(String.format("Looking into class %s : chksum pre-process is : %s", originatingClass, chkSumOrig));

		String source = null;
		String sourceBefore = null;
		String sourceAfter = null;
		try {
			source = IO.fileToString(srcFile.toString(), sourceEncoding);
			if (source.indexOf(commentHeaderStart) > -1) {
				sourceBefore = source.substring(0, source.indexOf(commentHeaderStart));
				sourceAfter = source.substring(source.indexOf(COMMENT_HEADER_END) + COMMENT_HEADER_END.length(), source.length());
			}
		} catch (IOException e) {
			throw new ProcessRuntimeException(String.format("Could not read source file %s", srcFile.getAbsolutePath()), e);
		}
		if (sourceBefore != null && sourceAfter != null) {
			this.completeSourceOrigSource = new StringBuilder(sourceBefore).append(sourceAfter).toString();
		} else {
			this.completeSourceOrigSource = source.replace(COMMENT_HEADER_END, "");
		}
	}

	String getOriginatingClass() {
		return originatingClass;
	}

	String getOriginatingClassShort() {
		return originatingShort;
	}

	void setChkSum(String chkSum) {
		int toPad = CHKSUM_LEN - chkSum.length();
		if (toPad < 0) {
			chkSum = chkSum.substring(0, CHKSUM_LEN);
			warn("    Oooops - chksum overflow....");
			this.chkSum = chkSum.substring(0, CHKSUM_LEN);
		} else {
			StringBuilder padding = new StringBuilder();
			while (toPad > 0) {
				padding.append("X");
				toPad--;
			}
			this.chkSum = chkSum + padding.toString();
		}
		info("    new chksum is : " + this.chkSum);
	}

	private String removeSpaceAndNewlines(String in) {
		return in.replace("}" + NL, "").replace(NL, "").replace(" ", "").replace("}", "");
	}

	void appendElementToWork(ElementToWork annotatedElement) {
		elementsToWork.add(annotatedElement);
	}

	Set<ElementToWork> getElementsToWork() {
		return elementsToWork;
	}

	File getSrcFile() {
		return srcFile;
	}

	void append(String s, String significantPart) {
		if (!IO.hasContents(significantPart) || (IO.hasContents(significantPart) && !completeSourceOrigSource.contains(significantPart))) {
			if (!completeSourceOrigSource.contains(s) && !sourceToAppend.toString().contains(s)) { // NOSONAR this is easier to read for me
				sourceToAppend.append(s);
				if (!s.contains("///////")) {
					hasAppended = true;
				}
			}
		}
	}

	void doAppend() {
		try {
			if (hasAppended || !chkSum.equals(chkSumOrig)) { // rewrite to the new format even if nothing was appended!
				String newSource = completeSourceOrigSource.substring(0, completeSourceOrigSource.lastIndexOf('}'));
				if (sourceEncoding != null) {
					newSource = new String(newSource.getBytes(), sourceEncoding);
				}
				if (!chkSum.equals(chkSumOrig)) {
					removePreviousAddition();
					info("  - steps have changed : RE-WRITING");
					sourceToAppend.replace(CHKSUM_POS - 2, CHKSUM_POS + CHKSUM_LEN + 1, chkSum);
					String appendedSrc = sourceToAppend.toString();
					String completeSrc = new StringBuilder(newSource).append(appendedSrc).toString();
					writeToFile(completeSrc);
				} else {
					info("    steps have not changed");
				}
			} else {
				info("    steps have not changed, nothing appended");
			}
		} catch (IOException e) {
			throw new ProcessRuntimeException("Could not append source", e);
		}
	}

	private void removePreviousAddition() {
		try {
			String currentSource = IO.fileToString(srcFile.getAbsolutePath(), sourceEncoding);
			if (currentSource.contains(COMMENT_HEADER_END)) {
				currentSource = currentSource.replace(originatingAppendeSource, "");
				writeToFile(currentSource);
			}
		} catch (IOException e) {
			throw new ProcessRuntimeException(String.format("Could not re-write original source file : %s ", srcFile.getAbsolutePath()), e);
		}
	}

	private void writeToFile(String completeSrc) throws IOException {
		if (sourceEncoding != null) {
			IO.overwriteStringToFileWithEncoding(srcFile.getAbsolutePath(), completeSrc, sourceEncoding.displayName());
		} else {
			IO.overwriteStringToFileWithEncoding(srcFile.getAbsolutePath(), completeSrc, null);
		}
	}

	@Override
	public String doToString() {
		return originatingClass;
	}

	void clear() {
		sourceToAppend = new StringBuilder();
	}

	void setSourceEncoding(Charset specifiedSourceCharset) {
		this.sourceEncoding = specifiedSourceCharset;
	}

	boolean matchStatusClass(Object classSymbol) {
		return completeSourceOrigSource.contains(classSymbol.toString());
	}

	boolean hasMatchedJavaScrFileForStatusClass() {
		return javaScrFileForStatusClass != null;
	}

	File getJavaScrFileForStatusClass() {
		return javaScrFileForStatusClass;
	}

	public void setJavaScrFileForStatusClass(File fileViaSunClassesReflectionHack) {
		this.javaScrFileForStatusClass = fileViaSunClassesReflectionHack;
	}
}

