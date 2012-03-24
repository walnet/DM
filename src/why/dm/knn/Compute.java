/**
 * Copyright (C) 2012 why
 */
package why.dm.knn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import why.dm.Document;
import why.dm.util.DocumentDouble;

/**
 * 
 * @author qinhuiwang 该类用于放置KNN所需的各种计算方法
 */
public class Compute {
	public static int NUM_KNN = 500;// knn算法中取出最近的NUM_KNN个点（即文档）

	/**
	 * 用各维度相减的差的平方和再开平方根作为文档的距离
	 * 
	 * @param d1
	 *            文档1
	 * @param d2
	 *            文档2
	 * @return 2个文档的距离
	 */
	public static Double computeDistance(Document d1, Document d2) {
		Double res;
		HashMap<Integer, Integer> hitsd1 = d1.getHits();
		HashMap<Integer, Integer> hitsd2 = d2.getHits();
		HashMap<Integer, Integer> differHits = new HashMap<Integer, Integer>();// 维度差

		int l;// temporarily store the value
		int sum = 0;
		Iterator<Integer> hitsd1iIterator = hitsd1.keySet().iterator();
		while (hitsd1iIterator.hasNext()) {// 遍历文档1，作为被减数
			Integer d1Key = (Integer) hitsd1iIterator.next();
			differHits.put(d1Key, hitsd1.get(d1Key));
		}
		Iterator<Integer> hitsd2iIterator = hitsd2.keySet().iterator();
		while (hitsd2iIterator.hasNext()) {// 遍历文档2，作为减数
			Integer d2Key = (Integer) hitsd2iIterator.next();
			if (differHits.containsKey(d2Key))
				differHits
						.put(d2Key, differHits.get(d2Key) - hitsd2.get(d2Key));
			else
				differHits.put(d2Key, hitsd2.get(d2Key));
		}
		Iterator<Integer> differHitsiIterator = differHits.keySet().iterator();
		while (differHitsiIterator.hasNext()) {// 取出所有的维度
			Integer key = (Integer) differHitsiIterator.next();
			l = differHits.get(key);
			sum += l * l;// 平方和
		}
		/*
		 * for (int i = 0; i < dimension; i++) { if (hitsd1.containsKey(i)) j =
		 * hitsd1.get(i); else j = 0;
		 * 
		 * if (hitsd2.containsKey(i)) k = hitsd2.get(i); else k = 0; if (k != 0
		 * || j != 0) { l = j - k; sum += l * l;// 平方和 } }
		 */
		res = sum + 0.0;// 原来应该Math.sqrt(sum)，但为了提高计算速度，直接不开根号了
		return res;
	}

	/**
	 * 相乘方式计算2个文档距离
	 * 
	 * @param d1
	 *            文档1
	 * @param d2
	 *            文档2
	 * @param dimension
	 *            文档的维度数
	 * @return 2个文档的距离
	 */
	public static Double computeDistanceByProduct(Document d1, Document d2) {
		return computeDistanceByProduct(d1.getHits(), d2);
	}

	/**
	 * 相乘方式计算2个文档距离
	 * 
	 * @param hitsd1
	 *            文档1 weiduji
	 * @param d2
	 *            文档2
	 * @return 2个文档的距离
	 */
	public static Double computeDistanceByProduct(
			HashMap<Integer, Integer> hitsd1, Document d2) {
		Double res;
		HashMap<Integer, Integer> hitsd2 = d2.getHits();

		Integer j, k;// temporarily store the value
		int sum = 0;
		Iterator<Integer> iterator = hitsd1.keySet().iterator();
		while (iterator.hasNext()) {
			Integer key = iterator.next();
			j = hitsd1.get(key);
			if (null != (k = hitsd2.get(key))) {
				sum += k * j;
			}
		}
		res = sum + 0.0;
		return res;
	}

	/**
	 * 计算文档与中心点的乘积
	 * 
	 * @param docHits
	 *            文档的维度集合
	 * @param cp
	 *            中心点
	 * @return 乘积
	 */
	public static Double computeProductWithCenterPoint(
			HashMap<Integer, Integer> docHits, CenterPoint cp) {
		HashMap<Integer, Double> hitsd2 = cp.getNewHits();

		Integer j;// temporarily store the value
		Double k;
		Double sum = 0.0;
		Iterator<Integer> testDocHistIterator = docHits.keySet().iterator();
		while (testDocHistIterator.hasNext()) {
			Integer key = (Integer) testDocHistIterator.next();
			j = docHits.get(key);
			k = hitsd2.get(key);
			if (k != null)
				sum += k * j;
		}
		return sum;
	}

	/**
	 * 向量归一求距离
	 * 
	 * @param d1
	 *            文档1
	 * @param d2
	 *            文档2
	 * @param dimension
	 *            文档的维度数
	 * @return 2个文档的距离
	 */
	public static Double computeDistanceByUnification(Document d1, Document d2,
			int dimension) {
		Double res = -1.0;
		HashMap<Integer, Integer> hitsd1 = d1.getHits();
		HashMap<Integer, Integer> hitsd2 = d2.getHits();

		/***************** 以下***向量归一 ***************************/
		HashMap<Integer, Double> hitsd1New = new HashMap<Integer, Double>();
		HashMap<Integer, Double> hitsd2New = new HashMap<Integer, Double>();
		Double ss1 = 0.0, ss2 = 0.0;// sum of squares平方和
		for (int q = 0; q < dimension; q++) {
			if (hitsd1.containsKey(q))
				ss1 += Math.pow(hitsd1.get(q), 2);
			if (hitsd2.containsKey(q))
				ss2 += Math.pow(hitsd2.get(q), 2);
		}
		Double sqrt1 = Math.sqrt(ss1);
		Double sqrt2 = Math.sqrt(ss2);
		System.out.println("sqrt1=" + sqrt1 + "; sqrt2= " + sqrt2);
		for (int q = 0; q < dimension; q++) {
			if (hitsd1.containsKey(q))
				hitsd1New.put(q, hitsd1.get(q) / sqrt1);

			if (hitsd2.containsKey(q))
				hitsd2New.put(q, hitsd2.get(q) / sqrt2);
		}
		/***************** 以上***向量归一 ***************************/

		Double j, k;// temporarily store the value
		int sum = 0;
		for (int i = 0; i < dimension; i++) {
			if (hitsd1New.containsKey(i))
				j = hitsd1New.get(i);
			else
				j = 0.0;

			if (hitsd2New.containsKey(i))
				k = hitsd2New.get(i);
			else
				k = 0.0;
			if (k != 0.0 || j != 0.0)
				sum += Math.pow(j - k, 2);
		}
		res = Math.sqrt(sum);
		return res;
	}

	/**
	 * 计算文档长度length
	 * 
	 * @param doc
	 *            文档
	 */
	public static void computeDocLength(Document doc) {
		HashMap<Integer, Integer> dochits = doc.getHits();
		Set<Integer> set = dochits.keySet();
		Iterator<Integer> it = set.iterator();
		Double ss = 0.0;
		int temp;
		while (it.hasNext()) {
			temp = dochits.get(it.next());
			ss += temp * temp;
		}
		doc.setLength(Math.sqrt(ss));
	}

	/**
	 * 对某个中心点的所有维度进行归一化
	 * 
	 * @param cpHits
	 *            维度集合
	 * @param cpLength
	 *            中心点的长度
	 */
	public static HashMap<Integer, Double> computeHitsByUnification(
			HashMap<Integer, Double> cpHits, Double cpLength) {
		Set<Integer> set = cpHits.keySet();
		Iterator<Integer> it = set.iterator();
		while (it.hasNext()) {
			Integer i = it.next();
			cpHits.put(i, cpHits.get(i) / cpLength);
		}
		return cpHits;
	}

	/***
	 * 计算文档加权相似度
	 * 
	 * @param doc
	 *            文档
	 */
	// 取排最前面的NUM_KNN个来计算,即NUM_KNN个distance相加
	public static void computeSim(Document doc) {
		Double sim = 0.0;
		boolean run = true;
		int docClassify = doc.getClassify();
		int i = 0;

		Set<DocumentDouble> dis = doc.getDocumentDistances();
		Iterator<DocumentDouble> it = dis.iterator();
		while (it.hasNext() && run) {
			i++;
			if (i >= NUM_KNN)
				run = false;
			DocumentDouble di = it.next();
			if (docClassify == di.document.getClassify())
				sim += di.distance;
		}
		doc.setSim(sim);
		dis.clear();// 清空存储的距离数组
	}

	/**
	 * 计算中心点的函数
	 * 
	 * @param docList
	 *            一个类中的文档集
	 * @param dimension
	 *            维度数
	 * @return 计算得到的中心点
	 */
	public static CenterPoint computeCenterPoint(List<Document> docList,
			int dimension) {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		List<Double> numeratorList = new ArrayList<Double>();// 分子
		Double demominator = 0.0;// 分母
		// 初始化分子集合
		for (int i = 0; i < dimension; i++)
			numeratorList.add(0.0);

		int size = docList.size();
		Document doc = null;
		Double sim;
		for (int j = 0; j < size; j++) {// 遍历类中文档
			doc = docList.get(j);
			sim = doc.getSim();
			demominator += sim;// 分母累加
			HashMap<Integer, Integer> docHits = doc.getHits();
			for (int i = 0; i < dimension; i++)
				if (docHits.containsKey(i)) {// 分子每个维度进行累加
					numeratorList.set(i, numeratorList.get(i) + docHits.get(i)
							* sim);
				}
		}
		int classify = -1;// 得到该分类的索引
		if (doc != null)
			classify = doc.getClassify();
		// 组合到向量的维度集中
		HashMap<Integer, Double> newHits = new HashMap<Integer, Double>();
		for (int i = 0; i < dimension; i++) {
			Double d = numeratorList.get(i);
			if (d != 0.0)
				newHits.put(i, d / dimension);// 实际应该是demominator
		}
		return new CenterPoint(newHits, classify);
	}

	/**
	 * 计算中心点：利用类中所有点的各维度和来直接取平均值
	 * 
	 * @param docList
	 *            一个类中的文档集
	 * @param isUnification
	 *            是否要对中心点进行归一化，true为是
	 * @param whetherUseIdfs
	 *            是否使用idfs,true为是
	 * @param idfs
	 *            idfs的集合，如果不使用它，可以传入NULL
	 * @return 计算得到的中心点
	 */
	public static CenterPoint computeCenterPointByAverage(
			LinkedList<Document> docList, boolean isUnification,
			boolean whetherUseIdfs, HashMap<Integer, Double> idfs) {

		// List<Double> dimenList = new ArrayList<Double>();
		HashMap<Integer, Double> cpHits = new HashMap<Integer, Double>();
		// 初始化维度集合
		// for (int i = 0; i < dimension; i++)
		// dimenList.add(0.0);

		// 以下 统计总和
		Document doc = null;
		Iterator<Document> iterator = docList.iterator();
		while (iterator.hasNext()) {// 一个个取出文档
			doc = iterator.next();
			HashMap<Integer, Integer> docHits = doc.getHits();
			Iterator<Integer> keyiIterator = docHits.keySet().iterator();
			while (keyiIterator.hasNext()) {// 遍历当前文档的所有维度
				Integer key = (Integer) keyiIterator.next();
				// dimenList.set(key, dimenList.get(key) + docHits.get(key));
				if (cpHits.containsKey(key))
					cpHits.put(key, cpHits.get(key) + docHits.get(key));// 维度
																		// 和
				else
					cpHits.put(key, docHits.get(key) + 0.0);
			}
		}
		Double cpLength = 0.0;// 用于存储中心点长度，以便于归一中心点时使用
		// HashMap<Integer, Double> cpHits = new HashMap<Integer, Double>();
		int numOfDocs = docList.size();
		Iterator<Integer> cpKeyiIterator = cpHits.keySet().iterator();
		while (cpKeyiIterator.hasNext()) {// 取平均值
			Integer cpkey = (Integer) cpKeyiIterator.next();
			Double tmp = cpHits.get(cpkey) / numOfDocs;
			if (whetherUseIdfs) {// 如果使用idfs，则执行下一句
				System.out.println(cpkey + "---size of idfs: " + idfs.size());
				tmp = tmp * idfs.get(cpkey);// 乘以相应的权重idfs
			}
			cpHits.put(cpkey, tmp);
			cpLength += tmp * tmp;

		}
		cpLength = Math.sqrt(cpLength);
		if (isUnification)//
			cpHits = computeHitsByUnification(cpHits, cpLength);// 对中心点维度进行归一化
		int classify = -2;
		if (doc != null)
			classify = doc.getClassify();
		return new CenterPoint(cpHits, classify, cpLength);
	}

	/**
	 * 用维度的内积来计算测试文档与中心点的相似度，值越大，则越相似
	 * 
	 * @param d
	 *            测试文档
	 * @param cp
	 *            中心点
	 * @param dimension
	 *            维度数
	 * @return 相似度值
	 */
	public static Double computeSimWithCenterPoint(Document d, CenterPoint cp) {
		HashMap<Integer, Integer> hitsd = d.getHits();
		HashMap<Integer, Double> hitscp = cp.getNewHits();

		Integer j;
		Double k;// temporarily store the value
		Double sum = 0.0;
		Iterator<Integer> testDocHitsIterator = hitsd.keySet().iterator();
		while (testDocHitsIterator.hasNext()) {
			Integer key = (Integer) testDocHitsIterator.next();
			j = hitsd.get(key);
			k = hitscp.get(key);
			if (k != null)
				sum += k * j;
		}
		return sum;
	}

	/**
	 * 用向量距离的平方来表示测试文档与中心点的相似度，值越小，则越相似
	 * 
	 * @param d
	 *            测试文档
	 * @param cp
	 *            中心点
	 * @param dimension
	 *            维度数
	 * @return 相似度值
	 */
	public static Double computeSimWithCenterPointByDistance(Document d,
			CenterPoint cp, int dimension) {
		HashMap<Integer, Integer> hitsd = d.getHits();
		HashMap<Integer, Double> hitscp = cp.getNewHits();

		Integer j;
		Double k;// temporarily store the value
		Double sum = 0.0;
		for (int i = 0; i < dimension; i++) {
			j = hitsd.get(i);

			k = hitscp.get(i);

			if (k != null && j != null) {
				Double tmp = k - j;
				sum += tmp * tmp;
			}
		}
		return sum;
	}

	/**
	 * 用余弦公式来计算测试文档与中心点的相似度，值越大，则越相似
	 * 
	 * @param docHits
	 *            文档的维度集
	 * @param docLength
	 *            文档长度
	 * @param cp
	 *            中心点
	 * @return 余弦值
	 */
	public static Double computeSimWithCenterPointByCos(
			HashMap<Integer, Integer> docHits, Double docLength, CenterPoint cp) {
		Double numerator = computeProductWithCenterPoint(docHits, cp);// 分子
		Double denominator = docLength * cp.getLength();// 分母
		if (0 >= denominator) {
			throw new ArithmeticException();
		}
		return numerator / denominator;// 除以文档长度和中心点长度的乘积

	}

	/**
	 * 通过KNN（最邻近的K个文档中，拥有最多文档的类别）方法来计算文档所属的类
	 * 
	 * @param doc
	 *            测试文档
	 * @param NumOfClassify
	 *            类别数
	 * @return 所属类别的序号
	 */
	public static int findClassifyByKnnWithProduct(Document doc,
			int NumOfClassify) {
		List<Integer> classifyList = new ArrayList<>();// 存储每个类别出现的文档个数，索引为类别的序号，值为出现的文档个数
		int size = classifyList.size();
		for (int i = 0; i < NumOfClassify; i++)
			classifyList.add(0);// 初始化为0
		Set<DocumentDouble> documentDistances = doc.getDocumentDistances();// 距离（或相似度）和文档（指针）的集合
		Iterator<DocumentDouble> iteratorDocumentDistances = documentDistances
				.iterator();
		int k = 0;// 统计取出了多少个最邻近的文档
		while (iteratorDocumentDistances.hasNext() && k <= NUM_KNN) {
			DocumentDouble documentDistance = (DocumentDouble) iteratorDocumentDistances
					.next();
			int classifyTemp = documentDistance.document.getClassify();
			classifyList.set(classifyTemp, classifyList.get(classifyTemp) + 1);// 根据取出的文档类别对统计数组进行加1
			k++;
		}

		int max = -2;
		int classify = -3;
		for (int i = 0; i < NumOfClassify; i++) {
			int NumOfOneClassify = classifyList.get(i);// 获得每个类别统计的文档个数
			if (NumOfOneClassify > max) {
				max = NumOfOneClassify;
				classify = i;
			}
		}
		doc.setDocumentDistances(null);// 清空docDI集合，它仅服务于此处的KNN查找
		return classify;
	}

}
