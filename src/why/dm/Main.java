/**
 * Copyright (C) 2012 why
 */
package why.dm;

/**
 * @version $Rev$ $Date$
 * @abstract The Main class.
 * @author hector
 */
public final class Main {

	/**
	 * Entry function.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FeatureExtraction featureExtraction = new FeatureExtraction();
		featureExtraction.extractFeacture("bin/newgroups");
		featureExtraction.trace();
	}

}
