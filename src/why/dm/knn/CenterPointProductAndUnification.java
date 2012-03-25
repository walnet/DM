/**
 * Copyright (C) 2012 why
 */
package why.dm.knn;

import java.util.Iterator;
import java.util.LinkedList;

import why.dm.Classifier;
import why.dm.Document;
import why.dm.Feature;

/**
 * The CenterPoint_ProductAndUnification classifier class
 * 
 * @author qinhuiwang
 * @version $Rev$ $Date$
 */
public class CenterPointProductAndUnification extends Classifier {

	LinkedList<CenterPoint> centerPoints = new LinkedList<CenterPoint>();

	/*
	 * @see why.dm.Classifier#clear()
	 */
	@Override
	public void clear() {
		centerPoints.clear();
	}

	/*
	 * @see why.dm.Classifier#test()
	 */
	@Override
	public void test() {
		redirectToNewOutput("test_" + debugFileName + ".txt");
		System.out.println("预测结果：");
		LinkedList<Document> testDocs = featureExtraction.getTestDocuments();
		// ComputeAllDocuments.computeLengthOfEachDocs(testDocs);//
		// 服务于cos值的相似度，其他的可以注释掉
		int forecastRight = 0;
		int current = 0;
		Iterator<Document> iterator = testDocs.iterator();
		initResultMatrix();// 初始化结果矩阵
		while (iterator.hasNext()) {
			Document doc = iterator.next();
			int forecastClassify = ComputeAllDocuments
					.findClassifyByCenterPoints(doc, centerPoints);// 预测的所属类

			++current;
			if (0 == current % 40)
				System.out.print(current + " ");// System.out.print(doc.getPath()+"-----准确类别="+doc.getClassify()+";  预测的类别="+forecastClassify);
			if (0 == current % 400)
				System.out.println();
			if (-2 == forecastClassify)// 出错
				System.err.println("ERROR: " + doc.getPath());
			int realClassify = doc.getClassify();
			accumulateResultMatrix(forecastClassify, realClassify);// 计算结果矩阵个元素值，用于矩阵输出
			if (realClassify == forecastClassify)
				++forecastRight;
		}
		System.out.println();
		int size = testDocs.size();
		System.out.println("\n" + forecastRight + " correct of total " + size
				+ "(" + (forecastRight / (size + 0.0)) + ")");
		calculateResultMatrix(); // 计算比例
		outputResultMaxtrix();// 输出结果矩阵
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
		Feature trainingFeature = featureExtraction.getTrainingFeature();
		ComputeAllDocuments.computeAllCenterPointsByAverage(centerPoints,
				trainingFeature.getClassifyDocuments(), true, false, null);// 中心点集合
		System.out.println("CenterPoints size: " + centerPoints.size());
		redirectToOldOutput();
	}

}
