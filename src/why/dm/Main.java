/**
 * 
 */
package why.dm;

/**
 * @abstract The Main class.
 * @author hector
 *
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
