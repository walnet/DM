/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import why.dm.knn.DocDI;

/**
 * The Document class
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public class Document {
	// 所属分类
	private int classify = -1;
	// 相对于每个类别的相似度
	private ArrayList<Double> classifyValues = new ArrayList<Double>();
	// Only for debug.
	private LinkedList<Integer> hitIndices = new LinkedList<Integer>();
	// 包括序号和计数
	private HashMap<Integer, Integer> hits = new HashMap<Integer, Integer>();
	// 文件路径
	private String path;
	// the distances between this doc with the rest docs 该文档与其他文档的距离，以及其他文档的指针
	private Set<DocDI> docDIs = null;
	// the length of doc
	private Double length = -1.0;

	// 文档与其所属类的相似度(加权相似度)
	private Double sim = -1.0;

	public void addClassifyValue(double value) {
		classifyValues.add(value);
	}

	/**
	 * @return the classify
	 */
	public int getClassify() {
		return classify;
	}

	/**
	 * @return the classifyValues
	 */
	public ArrayList<Double> getClassifyValues() {
		return classifyValues;
	}

	/**
	 * @return the docDIs
	 */
	public Set<DocDI> getDocDIs() {
		return docDIs;
	}

	/**
	 * @return the hits
	 */
	public HashMap<Integer, Integer> getHits() {
		return hits;
	}

	/**
	 * @return the length
	 */
	public Double getLength() {
		return length;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the sim
	 */
	public Double getSim() {
		return sim;
	}

	public void insertTerm(Integer index) {
		hitIndices.push(index);
		if (hits.containsKey(index)) {
			Integer count = hits.remove(index);
			hits.put(index, ++count);
		} else {
			hits.put(index, 1);
		}
	}

	/**
	 * @param classify
	 *            the classify to set
	 */
	public void setClassify(int classify) {
		this.classify = classify;
	}

	/**
	 * @param docDIs
	 *            the docDIs to set
	 */
	public void setDocDIs(Set<DocDI> docDIs) {
		this.docDIs = docDIs;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(Double length) {
		this.length = length;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @param sim
	 *            the sim to set
	 */
	public void setSim(Double sim) {
		this.sim = sim;
	}

	public void trace() {
		System.out.print("Class " + classify + ">> ");
		Iterator<Integer> iter = hitIndices.descendingIterator();
		while (iter.hasNext()) {
			Integer index = iter.next();
			System.out.print(index + ": " + hits.get(index) + "; ");
		}
		System.out.println();
	}

	public void trace(ArrayList<String> classifyNames,
			ArrayList<String> termNames) {
		String str = classifyNames.get(classify);
		int lastIndex = path.lastIndexOf('\\');
		int lastIndex2 = path.lastIndexOf('/');
		if (lastIndex2 > lastIndex)
			lastIndex = lastIndex2;
		System.out.print(str + "(" + classify + ") "
				+ path.substring(lastIndex + 1) + ">> ");
		Iterator<Integer> iter = hitIndices.descendingIterator();
		while (iter.hasNext()) {
			Integer index = iter.next();
			System.out.print(index + "(" + termNames.get(index) + "): "
					+ hits.get(index) + "; ");
		}
		System.out.println();
	}

}
