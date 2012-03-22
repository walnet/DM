package why.dm.knn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import why.dm.Document;
import why.dm.Feature;
import why.dm.FeatureExtraction;

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
	public static void ComputeAllDistanceBetweenDocs(FeatureExtraction fe) {
		List<Document> docList = new ArrayList<Document>();// 所有文档

		Feature trainingFeature = fe.getTrainingFeature();
		ArrayList<LinkedList<Document>> classifyDocs = trainingFeature
				.getClassifyDocuments();
		int dimension = trainingFeature.getHits().size();// 训练集总维度数

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
			Set<DocDI> docDIs = new TreeSet<DocDI>(new DocDIDescComparator());// 自定义比较规则,使其降序排列
			distances[k] = new Double[NUM_OF_DOCS];// 初始化第二维度
			if (k > 0) {
				// 由后面的计算可知，只计算当前文档与下一个序号文档的距离，
				// 与序号在当前序号之前的文档的距离其实在前面已经计算过，
				// 所以distances[k][i]=distances[i][k]
				for (int i = 0; i < k; i++) {
					DocDI di2 = new DocDI(docList.get(i), distances[i][k]);
					docDIs.add(di2);
				}
			}
			/*************** 计算文档长度length *********************/
			// Compute.computeDocLength(doc,dimension);
			/*************** 计算文档长度length *********************/

			for (int m = k + 1; m < docList.size(); m++) {
				Document tempDoc = docList.get(m);
				Double len = Compute.computeDistanceByMultiply(doc, tempDoc,
						dimension);// 计算2文档间的距离
				distances[k][m] = len;// 赋值
				DocDI di = new DocDI(tempDoc, len);
				docDIs.add(di);
			}
			doc.setDocDIs(docDIs);
			/********** 以下计算每个文档与自己所在类的加权相似度 ***********/
			Compute.computeSim(doc);
		}
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
	public static ArrayList<CenterPoint> computeAllCenterPoints(
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
	 * @param dimension
	 *            维度集
	 * @return 中心点集合
	 */
	public static void computeAllCenterPointsByAverage(
			LinkedList<CenterPoint> centerPoints,
			ArrayList<LinkedList<Document>> classifyDocs, int dimension) {
		CenterPoint cp;
		for (int i = 0; i < classifyDocs.size(); i++) {
			cp = Compute.computeCenterPointByAverage(classifyDocs.get(i),
					dimension);
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
	 * @param dimension
	 *            维度数
	 * @return 所属类的序号/索引
	 */
	public static int findClassifyByCenterPoints(Document d,
			List<CenterPoint> cps, int dimension) {
		Double max = Double.NEGATIVE_INFINITY;
		Double min = Double.MAX_VALUE;
		Double s;
		int classify = -2;
		int size = cps.size();

		for (int i = 0; i < size; i++) {
			CenterPoint cp = cps.get(i);
			s = Compute.computeSimWithCenterPoint(d, cp, dimension);//
			// 前提是要对中心点进行归一化（computeCenterPointByAverage()中），计算比较准确
			if (s > max) {
				max = s;
				classify = cp.getClassify();
			}
			/*
			 * s=Compute.(d, cp, dimension);
			 * //前提是不能对中心点进行归一化（computeCenterPointByAverage()中），否则计算不准确
			 * if(s<min){ min=s; classify = cp.getClassify(); }
			 */
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
	 * @param dimension
	 *            维度值
	 * @return 预测出的类别序号
	 */
	public static int findClassifyByCenterPointsWithCOS(Document d,
			LinkedList<CenterPoint> cps, int dimension) {
		HashMap<Integer, Integer> docHits = d.getHits();
		Double docLength = d.getLength();
		Double max = Double.NEGATIVE_INFINITY;// 负无限小
		int classify = -2;
		Iterator<CenterPoint> iter = cps.iterator();
		while (iter.hasNext()) {
			CenterPoint cp = iter.next();
			Double tmpDouble = Compute.computeSimWithCenterPointByCOS(docHits,
					docLength, cp, dimension);
			if (tmpDouble > max) {
				max = tmpDouble;
				classify = cp.getClassify();
			}
		}
		return classify;
	}

	/**
	 * 计算指定集合中各文档自己的长度
	 * 
	 * @param docs
	 *            文档集合
	 */
	public static void computeLengthOfEachDocs(LinkedList<Document> docs) {
		Iterator<Document> iter = docs.iterator();
		while (iter.hasNext()) {
			Compute.computeDocLength(iter.next());
		}
	}

}
