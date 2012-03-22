package why.dm.knn;

import why.dm.Document;

/**
 * 
 * @author qinhuiwang 文档指针与距离类
 */
public class DocDI {
	public Document docIndex; // 拥有DOcDI对象的文档，和该文档来计算距离
	public Double distance; // 2文档的距离距离

	public DocDI(Document docIndex, Double distance) {
		this.docIndex = docIndex;
		this.distance = distance;
	}
}
