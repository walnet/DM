/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Naive Bayes classification
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public class NativeBayes {
	FeatureExtraction featureExtraction;

	// 计算结果
	// Pcs[j]表示j类的概率
	private ArrayList<Double> Pcs = new ArrayList<Double>();
	// classifyDiffs[j]表示j类不同词个数
	// private ArrayList<Integer> classifyDiffs = new ArrayList<Integer>();
	// classifyTotals[j]表示j类所有词出现次数总和
	// private ArrayList<Integer> classifyTotals = new ArrayList<Integer>();

	private boolean debugTrace = true;

	public void setFeatureExtraction(FeatureExtraction value) {
		featureExtraction = value;
	}

	public void classify() {
		for (int i = 0; featureExtraction.getClassifyNames().size() > i; ++i) {
			Pcs.add(getPc(i));
			// IntPtr diff = new IntPtr(0);
			// IntPtr total = new IntPtr(0);
			// getClassifyParam(i, diff, total);
			// classifyDiffs.add(diff.value);
			// classifyTotals.add(total.value);
			int total = 0;
			HashMap<Integer, Integer> currentclassifyHit = featureExtraction.getTrainingFeature().getClassifyHits().get(i);
			Iterator<Integer> iter = currentclassifyHit.keySet().iterator();
			while (iter.hasNext()) {
				Integer currentTerm = iter.next();
				total += currentclassifyHit.get(currentTerm);
			}
			if (debugTrace) {
				System.out
						.println(i + ">> Pc:" + Pcs.get(i) + "; diff: "
								+ currentclassifyHit.size() + "; total: "
								+ total + ".");
			}
		}
		Iterator<Document> iter = featureExtraction.getTestDocuments()
				.iterator();
		while (iter.hasNext()) {
			Document testDocument = iter.next();
			int totalClassify = featureExtraction.getClassifyNames().size();
			double maxPcd = 0;
			int maxClassify = -1;
			for (int currentClassify = 0; totalClassify > currentClassify; ++currentClassify) {
				double pcd = getPcd(testDocument, currentClassify);
				// 累乘转成log累加，都是负数，绝对值越大越强
				//if (maxPcd < pcd) {
				if (maxPcd > pcd) {
					maxPcd = pcd;
					maxClassify = currentClassify;
				}
			}
			if (debugTrace) {
				int lastIndex = testDocument.getPath().lastIndexOf('\\');
				int lastIndex2 = testDocument.getPath().lastIndexOf('/');
				if (lastIndex2 > lastIndex)
					lastIndex = lastIndex2;
				System.out.println("(" + testDocument.getClassify() + ") "
						+ testDocument.getPath()/* .substring(lastIndex + 1) */
						+ ">> " + maxClassify);
			}
			testDocument.setClassify(maxClassify);
		}
	}

	public double getPcd(Document testDocument, int classify) {
		// 累乘转成log累加
		//return getPdc(testDocument, classify) * Pcs.get(classify);
		return getPdc(testDocument, classify) + Math.log(Pcs.get(classify));
	}

	public double getPdc(Document testDocument, int classify) {
		double lastNumerator = 0;
		double lastDenominator = 0;
		double lastPwc = 0;
		// 累乘转成log累加
		//double pwc = 1;
		double pwc = 0;
		Iterator<Integer> iterTerm = testDocument.getHits().keySet().iterator();
		while (iterTerm.hasNext()) {
			Integer currentTerm;
			currentTerm = iterTerm.next();
			ArrayList<HashMap<Integer, Integer>> currentClassifyHits = featureExtraction.getTrainingFeature().getClassifyHits();
			HashMap<Integer, Integer> currentClassifyHit = currentClassifyHits.get(classify);
			
			// 当前词在当前类别中的出现次数
			Integer currentTermClassifyHit = currentClassifyHit.get(currentTerm);
			if (null == currentTermClassifyHit)
				currentTermClassifyHit = 0;
			
			// 当前类别所有词出现次数总和
			int totalClassifyHits = 0;
			Iterator<Integer> iterAllTerm = currentClassifyHit.keySet().iterator();
			while (iterAllTerm.hasNext()) {
				Integer hit = iterAllTerm.next();
				if (null == hit)
					hit = 0;
				totalClassifyHits += hit;
			}
			
			// 累乘转成log累加
			double numerator = 1 + currentTermClassifyHit;
			double denominator = currentClassifyHits.size() + totalClassifyHits;
			if (0 != denominator) {
				//System.out.println(lastDenominator);
				//System.out.println(lastPwc);
				//System.out.println(lastNumerator);
				//pwc *= numerator / denominator;
				pwc += Math.log(numerator / denominator);
			} else {
				pwc = -1;
			}
			lastNumerator = numerator;
			lastDenominator = denominator;
			lastPwc = pwc;
		}
		return pwc;
	}

	public double getPc(int classify) {
		int nc = 0;
		LinkedList<Document> currentClassifyDocuments = featureExtraction.getTrainingFeature().getClassifyDocuments().get(classify);
		for (int i = 0; currentClassifyDocuments.size() > i; ++i) {
			nc += currentClassifyDocuments.size();
		}
		double numerator = 1 + nc;
		double denominator = featureExtraction.getClassifyNames().size()
				+ featureExtraction.getTrainingFeature().getClassifyDocuments().size();
		if (0 != denominator) {
			return numerator / denominator;
		} else {
			return -1;
		}
	}

	/*
	 * public double getPwc(int classify, Integer term) { int n = 0;
	 * Iterator<Document> iterDocument = traningDocuments.iterator(); while
	 * (iterDocument.hasNext()) { Document currentDocument; if (classify ==
	 * (currentDocument = iterDocument.next()) .getClassify()) {
	 * HashMap<Integer, Integer> currentTerms; currentTerms =
	 * currentDocument.getHits(); Iterator<Integer> iterWord =
	 * currentTerms.keySet().iterator(); while (iterWord.hasNext()) { Integer
	 * currentTerm; currentTerm = iterWord.next(); int currentWordCount =
	 * currentTerms.get(currentTerm); if (term == currentTerm) { n +=
	 * currentWordCount; } } } }
	 * 
	 * double numerator = 1 + n; double denominator =
	 * classifyDiffs.get(classify) + classifyTotals.get(classify); if (0 !=
	 * denominator) { return numerator / denominator; } else { return -1; } }
	 */

	/*
	 * public void getClassifyParam(int classify, IntPtr diff, IntPtr total) {
	 * // 本类不同词个数 diff.value = 0; // 本类所有词出现次数总和 total.value = 0;
	 * HashSet<Integer> differentTerms = new HashSet<Integer>();
	 * Iterator<Document> iterDocument = traningDocuments.iterator(); while
	 * (iterDocument.hasNext()) { Document currentDocument; if (classify ==
	 * (currentDocument = iterDocument.next()) .getClassify()) {
	 * HashMap<Integer, Integer> currentTerms; currentTerms =
	 * currentDocument.getHits(); Iterator<Integer> iterWord =
	 * currentTerms.keySet().iterator(); while (iterWord.hasNext()) { Integer
	 * currentTerm; currentTerm = iterWord.next(); int currentWordCount =
	 * currentTerms.get(currentTerm); HashMap<Integer, Integer>
	 * currentClassifyHit = classifyHits .get(classify); int originWordCount =
	 * currentClassifyHit.get(currentTerm); currentClassifyHit.put(currentTerm,
	 * originWordCount + currentWordCount); total.value += currentWordCount; if
	 * (!differentTerms.contains(currentTerm)) {
	 * differentTerms.add(currentTerm); ++diff.value; } } } } }
	 */

}
