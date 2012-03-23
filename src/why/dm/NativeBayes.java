/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.lang.ArithmeticException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The Native Bayes classifier
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public class NativeBayes extends Classifier {
	// 计算结果
	// Pcs[j]表示j类的概率
	private ArrayList<Double> Pcs = new ArrayList<Double>();
	// classifyDiffs[j]表示j类不同词个数
	// private ArrayList<Integer> classifyDiffs = new ArrayList<Integer>();
	// classifyTotals[j]表示j类所有词出现次数总和
	// private ArrayList<Integer> classifyTotals = new ArrayList<Integer>();

	private int debugTotal = 0;
	private int debugCorrect = 0;

	/*
	 * @see why.dm.Classifier#clear()
	 */
	@Override
	public void clear() {
		Pcs.clear();
	}

	public double getPc(int classify) throws ArithmeticException {
		int total = 0;
		for (int i = 0; featureExtraction.getTrainingFeature()
				.getClassifyDocuments().size() > i; ++i) {
			total += featureExtraction.getTrainingFeature()
					.getClassifyDocuments().get(i).size();
		}
		double numerator = 1 + featureExtraction.getTrainingFeature()
				.getClassifyDocuments().get(classify).size();
		double denominator = featureExtraction.getClassifyNames().size()
				+ total;
		if (0 != denominator) {
			return numerator / denominator;
		} else {
			throw new ArithmeticException();
		}
	}

	public double getPcd(Document testDocument, int classify) {
		// 累乘转成log累加
		// P(d|c) * P(c)
		// 转成log
		// return getPdc(testDocument, classify) * Pcs.get(classify);
		return getPdc(testDocument, classify) + Math.log(Pcs.get(classify));
	}

	public double getPdc(Document testDocument, int classify)
			throws ArithmeticException {
		// double lastNumerator = 0;
		// double lastDenominator = 0;
		// double lastPwc = 0;
		// 累乘转成log累加
		// double pwc = 1;
		double pwc = 0;
		Iterator<Integer> termIterator = testDocument.getHits().keySet().iterator();
		while (termIterator.hasNext()) {
			Integer currentTerm;
			currentTerm = termIterator.next();
			HashMap<Integer, Integer> currentClassifyHit = featureExtraction
					.getTrainingFeature().getClassifyHits().get(classify);

			// 当前词在当前类别中的出现次数
			Integer currentTermClassifyHit = currentClassifyHit
					.get(currentTerm);
			if (null == currentTermClassifyHit)
				currentTermClassifyHit = 0;

			// 累乘转成log累加
			double numerator = 1 + currentTermClassifyHit;
			double denominator = currentClassifyHit.keySet().size()
					+ featureExtraction.getTrainingFeature()
							.getClassifyTotalHits().get(classify);
			if (0 != denominator) {
				// System.out.println(lastDenominator);
				// System.out.println(lastPwc);
				// System.out.println(lastNumerator);
				// pwc *= numerator / denominator;
				pwc += Math.log(numerator / denominator);
			} else {
				// pwc = -1;
				throw new ArithmeticException();
			}
			// lastNumerator = numerator;
			// lastDenominator = denominator;
			// lastPwc = pwc;
		}
		return pwc;
	}

	/*
	 * @see why.dm.Classifier#test()
	 */
	@Override
	public void test() {
		redirectToNewOutput("test_" + debugFileName + ".txt");
		for (int classify = 0; featureExtraction.getClassifyNames().size() > classify; ++classify) {
			Pcs.add(getPc(classify));
			// IntPtr diff = new IntPtr(0);
			// IntPtr total = new IntPtr(0);
			// getClassifyParam(i, diff, total);
			// classifyDiffs.add(diff.value);
			// classifyTotals.add(total.value);
			if (debugTrace) {
				System.out.println(classify
						+ ">> Pc:"
						+ Pcs.get(classify)
						+ "; diff: "
						+ featureExtraction.getTrainingFeature()
								.getClassifyHits().get(classify).keySet()
								.size()
						+ "; total: "
						+ featureExtraction.getTrainingFeature()
								.getClassifyTotalHits().get(classify) + ".");
			}
		}
		if (debugTrace) {
			initResultMatrix(); // 初始化结果矩阵
		}
		Iterator<Document> iterator = featureExtraction.getTestDocuments()
				.iterator();
		while (iterator.hasNext()) {
			Document testDocument = iterator.next();
			int totalClassify = featureExtraction.getClassifyNames().size();
			double maxPcd = Double.NEGATIVE_INFINITY;
			int maxClassify = -1;
			for (int currentClassify = 0; totalClassify > currentClassify; ++currentClassify) {
				double pcd = getPcd(testDocument, currentClassify); // P(c|d)
				testDocument.addClassifyValue(pcd);
				// 累乘转成log累加，都是负数
				if (maxPcd < pcd) {
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
						+ testDocument.getPath().substring(lastIndex + 1)
						+ ">> " + maxClassify);
				++debugTotal;
				if (maxClassify == testDocument.getClassify()) {
					++debugCorrect;
				}
				accumulateResultMatrix(maxClassify, testDocument.getClassify()); // 计算结果矩阵个元素值，用于矩阵输出
			}
			testDocument.setGuessClassify(maxClassify);
		}
		if (debugTrace) {
			System.out.println("Finished: " + debugCorrect
					+ " correct of total " + debugTotal + "("
					+ (double) debugCorrect / (double) debugTotal + ").");
			calculateResultMatrix(); // 计算比例
			outputResultMaxtrix(); // 输出结果矩阵
		} else {
			System.out.println("Finished!");
		}
		redirectToOldOutput();
	}

	/*
	 * @see why.dm.Classifier#trace()
	 */
	@Override
	public void trace() {
		// TODO Auto-generated method stub

	}

	/*
	 * @see why.dm.Classifier#train()
	 */
	@Override
	public void train() {
		redirectToNewOutput("train_" + debugFileName + ".txt");
		System.out.println("Nothing to train.");
		redirectToOldOutput();
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
