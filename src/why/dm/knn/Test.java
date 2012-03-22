package why.dm.knn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Test {
	public static void main(String[] args) {
		/*
		 * Map<Double, Integer> map =new TreeMap<Double, Integer>();
		 * map.put(6.7854,8); map.put(3.5435,7); map.put(3.5435,9);
		 * map.put(7.1174,6); map.put(8.9369,5); map.put(2.1146,4);
		 * map.put(3.1124,3); map.put(4.1164,2); map.put(5.1167,1);
		 * 
		 * Set set =map.keySet(); Iterator it= set.iterator(); while
		 * (it.hasNext()) { Double i = (Double)it.next();
		 * System.out.println(i+":"+map.get(i));
		 * 
		 * }
		 */

		/*
		 * Set<DocDI> set = new TreeSet<DocDI>(new DocDIComparator());
		 * set.add(new DocDI(1, 9.99)); set.add(new DocDI(2, 4.321));
		 * set.add(new DocDI(3, 5.678)); set.add(new DocDI(4, 8.324));
		 * set.add(new DocDI(5, 6.789)); set.add(new DocDI(6, 5.678));
		 * set.add(new DocDI(7, 6.766));
		 * 
		 * Iterator it=set.iterator(); while (it.hasNext()) { DocDI i =
		 * (DocDI)it.next(); System.out.println(i.index+":"+i.distance);
		 * 
		 * }
		 */
		if (4 >= 4)
			System.out.println(">= good job");

		System.out.println(Math.sqrt(225));// 开平方、开根号
		System.out.println(Math.pow(2 - 6, 2));// 求N次方

		List list = new ArrayList();
		list.add(5);
		list.add(1);
		list.add(4);
		list.add(2);
		list.add(3);
		list.add(9);
		list.add(7);
		list.add(8);

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		Double[][] distances = new Double[2][3];
		distances[0][0] = 1.0;
		distances[0][1] = 2.0;
		distances[0][2] = 3.0;
		distances[1][0] = 4.0;
		distances[1][1] = 5.0;
		distances[1][2] = 6.0;
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 3; j++)
				System.out.println("distances[" + i + "][" + j + "]="
						+ distances[i][j]);

		Double[][] distances2 = new Double[18828][];
		distances2[3] = new Double[18828];
		if (3 == 3 && 5 > 3)
			System.out.println("&&");
		int i = 500;
		if (i % 50 == 0)
			System.out.println("i 是 50的倍数");

		Set<Integer> s = new TreeSet<Integer>(new IntegerComparator());
		s.add(5);
		s.add(4);
		s.add(3);
		s.add(2);
		s.add(1);
		s.add(4);
		Iterator<Integer> it = s.iterator();
		while (it.hasNext())
			System.out.println(it.next());
		s.clear();
		System.out.println("output s again:");
		Iterator<Integer> it2 = s.iterator();
		while (it2.hasNext())
			System.out.println(it2.next());

		Double a = -1.0;
		if (a == -1.0)
			System.out.println("a is " + a);
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(0, 100);
		System.out.println(map.get(0));

		Integer q = map.get(1);
		Integer r = 3;
		System.out.println(q);
		if (q == null)
			System.out.println("q is null");
		if (r == 3)
			System.out.println("r is 3");
		System.out.println("数20000在输出：");
		for (int c = 0; c < 20000000; c++)
			i++;
		System.out.println(i);
		System.out.println("数完");

		String str = "abcd0efegheab";
		/*
		 * for(int i2=0;i2<str.length();i2++)
		 * System.out.println(str.charAt(i2));
		 */
		char[] strs = str.toCharArray();
		for (int i2 = 0; i2 < str.length(); i2++)
			System.out.println(strs[i2]);
		findSameChar(str);
		for (int i2 = 0; i2 < str.length(); i2++)
			System.out.println(strs[i2]);
		splitDemo("http://www.google.com.hk");
		HashMap<Integer, Double> cpHits = new HashMap<Integer, Double>();
//		cpHits.put(2, 3.0);
//		cpHits.put(3, 3.0);
//		cpHits.put(2, cpHits.get(2)/9);
		System.out.println(Double.MAX_VALUE>10000000);
	}

	/**
	 * 查找字符串中相同字母个数
	 * @param str
	 */
	public static void findSameChar(String str) {
		char[] cs = str.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] != 0) {
				int n = 0;
				char a = cs[i];
				for (int j = i; j < cs.length; j++) {
					if (a == cs[j]) {
						n++;
						cs[j] = 0;
					}
				}
				System.out.println(a + ":" + n);
			}
		}
	}
	
	public static void splitDemo(String addr){
		String[] strs = addr.split("//");
		if(strs.length>1)
			System.out.println(strs[1]);
	}

}

class IntegerComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer o1, Integer o2) {
		// TODO Auto-generated method stub
		if (o1 >= o2)
			return -1;
		else
			return 1;
	}

}
