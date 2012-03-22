package why.dm.knn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import why.dm.Document;

/**
 * 
 * @author qinhuiwang 该类用于放置KNN所需的各种计算方法
 */
public class Compute {
	public static int NUM_KNN = 200;// knn算法中取出最近的NUM_KNN个点（即文档）

	/**
	 * 
	 * @param d1
	 *            文档1
	 * @param d2
	 *            文档2
	 * @param dimension
	 *            文档的维度数
	 * @return 2个文档的距离
	 */
	public static Double computeDistance(Document d1, Document d2, int dimension) {
		Double res = -1.0;
		HashMap<Integer, Integer> hitsd1 = d1.getHits();
		HashMap<Integer, Integer> hitsd2 = d2.getHits();

		int j, k, l;// temporarily store the value
		int sum = 0;
		for (int i = 0; i < dimension; i++) {
			if (hitsd1.containsKey(i))
				j = hitsd1.get(i);
			else
				j = 0;

			if (hitsd2.containsKey(i))
				k = hitsd2.get(i);
			else
				k = 0;
			if (k != 0 || j != 0) {
				l = j - k;
				sum += l * l;// 平方和
			}
		}
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
	public static Double computeDistanceByMultiply(Document d1, Document d2,
			int dimension) {
		return computeDistanceByMultiply(d1.getHits(), d2, dimension);
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
	public static Double computeDistanceByMultiply(
			HashMap<Integer, Integer> hitsd1, Document d2, int dimension) {
		Double res;
		HashMap<Integer, Integer> hitsd2 = d2.getHits();

		Integer j, k;// temporarily store the value
		int sum = 0;
		for (int i = 0; i < dimension; i++) {
			j = hitsd1.get(i);

			k = hitsd2.get(i);

			if (k != null && j != null)
				sum += k * j;
		}
		res = sum + 0.0;
		return res;
	}

	/**
	 * 计算文档与中心点的乘积
	 * 
	 * @param hitsd1
	 *            文档的维度集合
	 * @param cp
	 *            中心点
	 * @param dimension
	 *            维度数值
	 * @return 乘积
	 */
	public static Double computeProductWithCenterPoint(
			HashMap<Integer, Integer> hitsd1, CenterPoint cp, int dimension) {
		HashMap<Integer, Double> hitsd2 = cp.getNewHits();

		Integer j;// temporarily store the value
		Double k;
		Double sum = 0.0;
		for (int i = 0; i < dimension; i++) {
			j = hitsd1.get(i);

			k = hitsd2.get(i);

			if (k != null && j != null)
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

		Set<DocDI> dis = doc.getDocDIs();
		Iterator<DocDI> it = dis.iterator();
		while (it.hasNext() && run) {
			i++;
			if (i >= NUM_KNN)
				run = false;
			DocDI di = it.next();
			if (docClassify == di.docIndex.getClassify())
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
	 * @param dimension
	 *            维度数
	 * @return 计算得到的中心点
	 */
	public static CenterPoint computeCenterPointByAverage(
			LinkedList<Document> docList, int dimension) {
		List<Double> dimenList = new ArrayList<Double>();

		// 初始化维度集合
		for (int i = 0; i < dimension; i++)
			dimenList.add(0.0);

		Document doc = null;
		Iterator<Document> iter = docList.iterator();
		while (iter.hasNext()) {// 一个个取出文档
			doc = iter.next();
			HashMap<Integer, Integer> docHits = doc.getHits();
			for (int j = 0; j < dimension; j++) {
				if (docHits.containsKey(j))
					dimenList.set(j, dimenList.get(j) + docHits.get(j));
			}
		}
		Double cpLength = 0.0;// 用于存储中心点长度，以便于归一中心点时使用
		HashMap<Integer, Double> cpHits = new HashMap<Integer, Double>();
		for (int i = 0; i < dimension; i++) {
			Double d = dimenList.get(i);
			if (d != 0.0) {
				Double tmp = d / docList.size();
				cpHits.put(i, tmp);
				cpLength += tmp * tmp;
			}
		}
		cpLength = Math.sqrt(cpLength);
		// cpHits=computeHitsByUnification(cpHits,cpLength);//对中心点维度进行归一化
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
	public static Double computeSimWithCenterPoint(Document d, CenterPoint cp,
			int dimension) {
		HashMap<Integer, Integer> hitsd = d.getHits();
		HashMap<Integer, Double> hitscp = cp.getNewHits();

		Integer j;
		Double k;// temporarily store the value
		Double sum = 0.0;
		for (int i = 0; i < dimension; i++) {
			j = hitsd.get(i);

			k = hitscp.get(i);

			if (k != null && j != null)
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
	 * @param dimension
	 *            维度值
	 * @return 余弦值
	 */
	public static Double computeSimWithCenterPointByCOS(
			HashMap<Integer, Integer> docHits, Double docLength,
			CenterPoint cp, int dimension) {
		Double numerator = computeProductWithCenterPoint(docHits, cp, dimension);// 分子
		Double denominator = docLength * cp.getLength();// 分母
		if (0 >= denominator) {
			throw new ArithmeticException();
		}
		return numerator / denominator;// 除以文档长度和中心点长度的乘积

	}

}
