/**
 * Copyright (C) 2012 why
 */
package why.dm.knn;

import java.util.Iterator;
import java.util.LinkedList;

import com.sun.org.apache.bcel.internal.generic.NEW;

import why.dm.Classifier;
import why.dm.Document;
import why.dm.Feature;

/**
 * The KNN classifier class
 *
 * @author qinhuiwang
 * @version $Rev$ $Date$
 */
public class Knn extends Classifier {
	
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
		redirectToNewOutput("runtime/test_" + debugFileName + ".txt");
		System.out.println("预测结果：");
		LinkedList<Document> testDocs = featureExtraction.getTestDocuments();
		ComputeAllDocuments.computeLengthOfEachDocs(testDocs);// 服务于cos值的相似度，其他的可以注释掉
		int forecastRight = 0;
		int current = 0;
		Iterator<Document> iterator = testDocs.iterator();
		while(iterator.hasNext()) {
			Document doc = iterator.next();
			int forecastClassify = ComputeAllDocuments
					.findClassifyByCenterPointsWithCOS(doc, centerPoints, featureExtraction.getTrainingFeature().getHits().size());
			// .findClassifyByCenterPoints(doc, cps, dimension);// 预测的所属类
			if (0 == current % 40)
				System.out.print(current + " ");// System.out.print(doc.getPath()+"-----准确类别="+doc.getClassify()+";  预测的类别="+forecastClassify);
			if (0 == current % 400)
				System.out.println();
			if (-2 == forecastClassify)// 出错
				System.out.println("ERROR: " + doc.getPath());
			if (doc.getClassify() == forecastClassify)
				++forecastRight;
			++current;
		}
		System.out.println();
		int size = testDocs.size();
		System.out.println(forecastRight + " correct of total " + size + "("
				+ (forecastRight / (size + 0.0)) + ")");
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
		redirectToNewOutput("runtime/train_" + debugFileName + ".txt");
		Feature trainingFeature = featureExtraction.getTrainingFeature();
		ComputeAllDocuments.computeAllCenterPointsByAverage(centerPoints,
						trainingFeature.getClassifyDocuments(),
						trainingFeature.getHits().size());// 中心点集合
		System.out.println(centerPoints.toString());
		redirectToOldOutput();
	}

}
