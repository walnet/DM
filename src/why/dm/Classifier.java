/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * The Classifier abstract class
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public abstract class Classifier {
	protected FeatureExtraction featureExtraction;
	protected boolean debugTrace = true;
	protected String debugFileName;
	private PrintStream originPs = System.out;

	public abstract void clear();

	/**
	 * @return the debugFileName
	 */
	public String getDebugFileName() {
		return debugFileName;
	}

	/**
	 * @return the featureExtraction
	 */
	public FeatureExtraction getFeatureExtraction() {
		return featureExtraction;
	}

	/**
	 * @return the debugTrace
	 */
	public boolean isDebugTrace() {
		return debugTrace;
	}

	public void redirectToNewOutput(String fileFullName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileFullName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		PrintStream newPs = new PrintStream(bos, false);
		System.setOut(newPs);
	}

	public void redirectToOldOutput() {
		System.out.flush();
		System.setOut(originPs);
	}

	/**
	 * @param debugFileName the debugFileName to set
	 */
	public void setDebugFileName(String debugFileName) {
		this.debugFileName = debugFileName;
	}

	/**
	 * @param debugTrace
	 *            the debugTrace to set
	 */
	public void setDebugTrace(boolean debugTrace) {
		this.debugTrace = debugTrace;
	}

	/**
	 * @param featureExtraction
	 *            the featureExtraction to set
	 */
	public void setFeatureExtraction(FeatureExtraction featureExtraction) {
		this.featureExtraction = featureExtraction;
	}

	public abstract void test();

	public abstract void trace();

	public abstract void train();

}
