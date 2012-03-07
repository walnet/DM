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
	// Classify hits
	// 各类中包含的term命中次数。第一维是分类索引。HashMap中Key是term的全局索引，Value是类中命中总和次数
	private ArrayList<HashMap<Integer, Integer>> classifyHits = new ArrayList<HashMap<Integer, Integer>>();
	// Classify documents.
	// 各类中包含的文档。第一维是分类索引
	private ArrayList<LinkedList<Document>> classifyDocuments = new ArrayList<LinkedList<Document>>();
	// Classify total hits
	// 各类中的term总命中次数，也就是把所有一个类中的所有classifyHits加起来。第一维是分类索引
	private ArrayList<Integer> classifyTotalHits = new ArrayList<Integer>();

	public ArrayList<HashMap<Integer, Integer>> getClassifyHits() {
		return classifyHits;
	}

	public ArrayList<LinkedList<Document>> getClassifyDocuments() {
		return classifyDocuments;
	}

	public void setClassifyDocuments(ArrayList<LinkedList<Document>> value) {
		classifyDocuments = value;
	}
	
	public ArrayList<Integer> getClassifyTotalHits() {
		return classifyTotalHits;
	}

	public void clear() {
		classifyHits.clear();
		classifyDocuments.clear();
		classifyTotalHits.clear();
	}

}
