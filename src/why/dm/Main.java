/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.util.Date;

/**
 * The Main class
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public final class Main {

	/**
	 * Entry function
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Date begin = new Date();

		// Extract feacture
		FeatureExtraction featureExtraction = new FeatureExtraction();
		featureExtraction.extractFeacture("bin/newgroups");
		featureExtraction.setTestProportion(0.01);
		featureExtraction.selectTestDocuments();

		// Naive Bayes classification
		System.out.println();
		NativeBayes nativeBayes = new NativeBayes();
		nativeBayes.setFeatureExtraction(featureExtraction);
		nativeBayes.classify();
		Date end = new Date();

		// Show time spent
		long l = end.getTime() - begin.getTime();
		// System.out.println();
		// featureExtraction.trace();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long minute = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long second = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
		System.out.println("Used time: " + day + "d " + hour + "h " + minute
				+ "m " + second + "s.");
	}

}
