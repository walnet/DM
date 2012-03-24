/**
 * Copyright (C) 2012 why
 */
package why.dm.knn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import why.dm.Classifier;
import why.dm.Document;
import why.dm.Feature;
import why.dm.FeatureExtraction;
import why.dm.util.DocumentDouble;

/**
 * 
 * @author qinhuiwang 与main方法直接交互的所有文档计算类
 */
public class ComputeAllDocuments {
	public static List<Document> docs = null;
	public static int NUM_OF_DOCS = 18828;

	/**
	 * 计算所有训练集中文档之间的距离
	 * 
	 * @param fe
	 *            FeatureExtraction
	 */
	private static void ComputeAllDistanceBetweenDocs(FeatureExtraction fe) {
		List<Document> docList = new ArrayList<Document>();// 所有文档

		Feature trainingFeature = fe.getTrainingFeature();
		ArrayList<LinkedList<Document>> classifyDocs = trainingFeature
				.getClassifyDocuments();

		for (int i = 0; i < classifyDocs.size(); i++) {// 遍历每个类
			LinkedList<Document> ll = classifyDocs.get(i);
			for (int j = 0; j < ll.size(); j++) {// 遍历类里面每个文档
				docList.add(ll.get(j));
			}
		}

		docs = docList;
		System.out.println("计算距离的文档数：" + docList.size());
		Double[][] distances = new Double[NUM_OF_DOCS][];// 初始化第一维度

		for (int k = 0; k < docList.size(); k++) {
			if (k % 2 == 0)
				System.out.print(k + " ");// debug
			if (k % 40 == 0)
				System.out.println();// debug
			Document doc = docList.get(k);
			Set<DocumentDouble> documentDistances = new TreeSet<DocumentDouble>();// 自定义比较规则,使其降序排列
			distances[k] = new Double[NUM_OF_DOCS];// 初始化第二维度
			if (k > 0) {
				// 由后面的计算可知，只计算当前文档与下一个序号文档的距离，
				// 与序号在当前序号之前的文档的距离其实在前面已经计算过，
				// 所以distances[k][i]=distances[i][k]
				for (int i = 0; i < k; i++) {
					DocumentDouble di2 = new DocumentDouble(docList.get(i),
							distances[i][k]);
					documentDistances.add(di2);
				}
			}
			/*************** 计算文档长度length *********************/
			// Compute.computeDocLength(doc,dimension);
			/*************** 计算文档长度length *********************/

			for (int m = k + 1; m < docList.size(); m++) {
				Document tempDoc = docList.get(m);
				Double len = Compute.computeDistanceByProduct(doc, tempDoc);// 计算2文档间的距离
				distances[k][m] = len;// 赋值
				DocumentDouble di = new DocumentDouble(tempDoc, len);
				documentDistances.add(di);
			}
			doc.setDocumentDistances(documentDistances);
			/********** 以下计算每个文档与自己所在类的加权相似度 ***********/
			Compute.computeSim(doc);
		}
	}

	/**
	 * 计算每个测试文档与训练文档的距离（用向量积计算），存储在测试文档的DocDIs中
	 * 
	 * @param fe
	 *            FeatureExtraction对象
	 */
	public static void ComputeDistanceFromTestDocsToTrainDocs(
			Classifier classifier) {
		FeatureExtraction featureExtraction = classifier.getFeatureExtraction();
		List<Document> docList = new LinkedList<Document>();// 所有文档

		Feature trainingFeature = featureExtraction.getTrainingFeature();
		ArrayList<LinkedList<Document>> classifyDocs = trainingFeature
				.getClassifyDocuments();

		for (int i = 0; i < classifyDocs.size(); i++) {// 遍历每个类
			Iterator<Document> iterator = classifyDocs.get(i).iterator();
			while (iterator.hasNext()) {
				docList.add(iterator.next());
			}
		}

		classifier.initResultMatrix();// 初始化结果矩阵
		int currentDocument = 0;
		System.out.println("ComputeDistanceFromTestDocsToTrainDocs");
		LinkedList<Document> testDocs = featureExtraction.getTestDocuments();// 测试集
		Iterator<Document> testIterator = testDocs.iterator();// 测试集iterator
		Iterator<Document> trainIterator;
		int classsifyRight = 0;
		int numOfTestDocs = testDocs.size();
		// DocDIDescComparator comparator = new DocDIDescComparator();
		Document testDocument = null;
		Document lastTestDocument = null;
		while (testIterator.hasNext()) {
			lastTestDocument = testDocument;
			testDocument = testIterator.next();
			// Set<DocumentDouble> docDIs = new
			// TreeSet<DocumentDouble>(comparator);// 自定义比较规则,使其降序排列
			Set<DocumentDouble> documentDistances = new TreeSet<DocumentDouble>();// 自定义比较规则,使其降序排列
			trainIterator = docList.iterator();// 训练集合iterator
			while (trainIterator.hasNext()) {
				Document trainDoc = (Document) trainIterator.next();
				DocumentDouble di = new DocumentDouble(
						trainDoc,
						Compute.computeDistanceByProduct(testDocument, trainDoc));
				documentDistances.add(di);
			}

			testDocument.setDocumentDistances(documentDistances);

			int guessClassify = Compute.findClassifyByKnnWithProduct(
					testDocument, featureExtraction.getTrainingFeature()
							.getClassifyDocuments().size());
			int realClassify = testDocument.getClassify();
			classifier.accumulateResultMatrix(guessClassify, realClassify);// 计算结果矩阵个元素值，用于矩阵输出
			if (guessClassify == realClassify)
				classsifyRight++;

			if (null != lastTestDocument)
				lastTestDocument.setDocumentDistances(null);
			++currentDocument;
		}
		System.out.println("KNN: " + classsifyRight + " correct of total "
				+ numOfTestDocs + "("
				+ (classsifyRight / (numOfTestDocs + 0.0)) + ")");
		classifier.calculateResultMatrix(); // 计算比例
		classifier.outputResultMaxtrix();// 输出结果矩阵
	}

	/**
	 * 计算所有类的中心点
	 * 
	 * @param classifyDocs
	 *            按类分的文档集合
	 * @param dimension
	 *            维度
	 * @return 中心点集合
	 */
	private static ArrayList<CenterPoint> computeAllCenterPoints(
			ArrayList<LinkedList<Document>> classifyDocs, int dimension) {
		ArrayList<CenterPoint> cps = new ArrayList<CenterPoint>();
		CenterPoint cp;
		for (int i = 0; i < classifyDocs.size(); i++) {
			cp = Compute.computeCenterPoint(classifyDocs.get(i), dimension);
			cps.add(cp);
		}
		return cps;
	}

	/**
	 * 利用直接平均值的方法来计算所有的中心点
	 * 
	 * @param classifyDocs
	 *            按类分的文档集合
	 * @param isUnification
	 *            是否要对中心点进行归一化，true为是
	 * @return 中心点集合
	 */
	public static void computeAllCenterPointsByAverage(
			LinkedList<CenterPoint> centerPoints,
			ArrayList<LinkedList<Document>> classifyDocs, boolean isUnification) {
		CenterPoint cp;
		for (int i = 0; i < classifyDocs.size(); i++) {
			cp = Compute.computeCenterPointByAverage(classifyDocs.get(i),
					isUnification);
			centerPoints.add(cp);
		}
	}

	/**
	 * 利用计算出的中心点集合预测出测试文档属于哪个类
	 * 
	 * @param d
	 *            文档
	 * @param cps
	 *            中心点集合
	 * @return 所属类的序号/索引
	 */
	public static int findClassifyByCenterPoints(Document d,
			List<CenterPoint> cps) {
		Double max = Double.NEGATIVE_INFINITY;
		// Double min = Double.MAX_VALUE;
		Double s;
		int classify = -2;
		int size = cps.size();

		for (int i = 0; i < size; i++) {
			CenterPoint cp = cps.get(i);
			s = Compute.computeSimWithCenterPoint(d, cp);//
			// 前提是要对中心点进行归一化（computeCenterPointByAverage()中），计算比较准确
			if (s > max) {
				max = s;
				classify = cp.getClassify();
			}
		}
		return classify;

	}

	/**
	 * 使用余弦公式，利用计算出的中心点集合预测出测试文档属于哪个类
	 * 
	 * @param d
	 *            文档
	 * @param cps
	 *            中心点集合
	 * @return 预测出的类别序号
	 */
	public static int findClassifyByCenterPointsWithCOS(Document d,
			LinkedList<CenterPoint> cps) {
		HashMap<Integer, Integer> docHits = d.getHits();
		Double docLength = d.getLength();
		Double max = Double.NEGATIVE_INFINITY;// 负无限小
		int classify = -2;
		Iterator<CenterPoint> iterator = cps.iterator();
		while (iterator.hasNext()) {
			CenterPoint cp = iterator.next();
			Double tmpDouble = Compute.computeSimWithCenterPointByCOS(docHits,
					docLength, cp);
			if (tmpDouble > max) {
				max = tmpDouble;
				classify = cp.getClassify();
			}
		}
		return classify;
	}

	/**
	 * 通过KNN方法来预测文档所属类别
	 * 
	 * @param testDocs
	 *            测试文档集
	 * @param NumOfClassify
	 *            类别数
	 */
	private static void forecastClassifyByKnn(LinkedList<Document> testDocs,
			int NumOfClassify, Classifier cf) {
		int classsifyRight = 0;
		int numOfTestDocs = testDocs.size();
		cf.initResultMatrix();// 初始化结果矩阵
		int currentDocument = 0;
		Iterator<Document> testDocsIterator = testDocs.iterator();
		while (testDocsIterator.hasNext()) {
			Document document = testDocsIterator.next();
			int forecastClassify = Compute.findClassifyByKnnWithProduct(
					document, NumOfClassify);
			int realClassify = document.getClassify();
			cf.accumulateResultMatrix(forecastClassify, realClassify);// 计算结果矩阵个元素值，用于矩阵输出
			if (forecastClassify == realClassify)
				classsifyRight++;
			++currentDocument;
			System.out.print(currentDocument + " ");
		}
		System.out.println("KNN: " + classsifyRight + " correct of total "
				+ numOfTestDocs + "("
				+ (classsifyRight / (numOfTestDocs + 0.0)) + ")");
		cf.calculateResultMatrix(); // 计算比例
		cf.outputResultMaxtrix();// 输出结果矩阵
	}

	/**
	 * 计算指定集合中各文档自己的长度
	 * 
	 * @param docs
	 *            文档集合
	 */
	public static void computeLengthOfEachDocs(LinkedList<Document> docs) {
		Iterator<Document> iterator = docs.iterator();
		while (iterator.hasNext()) {
			Compute.computeDocLength(iterator.next());
		}
	}

}
