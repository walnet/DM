/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The Feature class
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public class Feature {
	// 所有文档
	private LinkedList<Document> documents = new LinkedList<Document>();
	// 各类中包含的文档。第一维是分类索引。内容同documents一致。
	private ArrayList<LinkedList<Document>> classifyDocuments = new ArrayList<LinkedList<Document>>();
	// term命中次数
	private HashMap<Integer, Integer> hits = new HashMap<Integer, Integer>();
	// 所有term命中总次数之和
	private int totalHit = 0;
	// 各类中包含的term命中次数。第一维是分类索引。HashMap中Key是term的全局索引，Value是类中命中总和次数
	private ArrayList<HashMap<Integer, Integer>> classifyHits = new ArrayList<HashMap<Integer, Integer>>();
	// 各类中的term总命中次数，也就是把所有一个类中的所有classifyHits加起来。第一维是分类索引
	private ArrayList<Integer> classifyTotalHits = new ArrayList<Integer>();

	public LinkedList<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(LinkedList<Document> value) {
		documents = value;
	}

	public ArrayList<LinkedList<Document>> getClassifyDocuments() {
		return classifyDocuments;
	}

	public void setClassifyDocuments(ArrayList<LinkedList<Document>> value) {
		classifyDocuments = value;
	}

	public HashMap<Integer, Integer> getHits() {
		return hits;
	}

	public void setHits(HashMap<Integer, Integer> value) {
		hits = value;
	}

	public int getTotalHit() {
		return totalHit;
	}

	public void setTotalHit(int value) {
		totalHit = value;
	}

	public ArrayList<HashMap<Integer, Integer>> getClassifyHits() {
		return classifyHits;
	}

	public void setClassifyHits(ArrayList<HashMap<Integer, Integer>> value) {
		classifyHits = value;
	}

	public ArrayList<Integer> getClassifyTotalHits() {
		return classifyTotalHits;
	}

	public void setClassifyTotalHits(ArrayList<Integer> value) {
		classifyTotalHits = value;
	}

	public void clear() {
		classifyHits.clear();
		classifyDocuments.clear();
		classifyTotalHits.clear();
		hits.clear();
	}

}
