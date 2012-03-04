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
	// Classify terms
	private ArrayList<HashMap<Integer, Integer>> classifyHits = new ArrayList<HashMap<Integer, Integer>>();
	// Classify documents
	private ArrayList<LinkedList<Document>> classifyDocuments = new ArrayList<LinkedList<Document>>();

	public ArrayList<HashMap<Integer, Integer>> getClassifyHits() {
		return classifyHits;
	}

	public ArrayList<LinkedList<Document>> getClassifyDocuments() {
		return classifyDocuments;
	}

	public void setClassifyDocuments(ArrayList<LinkedList<Document>> value) {
		classifyDocuments = value;
	}

	public void clear() {
		classifyHits.clear();
		classifyDocuments.clear();
	}

}
