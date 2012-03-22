package why.dm.knn;

import java.util.Comparator;

/**
 * 
 * @author qinhuiwang 用于为DocDI对象做比较，依据distance值,升序排列的比较器
 */
public class DocDIComparator implements Comparator<DocDI> {

	@Override
	public int compare(DocDI o1, DocDI o2) {
		// TODO Auto-generated method stub
		if (o1.distance >= o2.distance)
			return 1;
		else
			return -1;
	}

}
