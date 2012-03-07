/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * The Document class
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public class Document {
	// 文件路径
	private String path;
	// 所属分类
	private int classify = -1;
	// 相对于每个类别的相似度
	private ArrayList<Double> classifyValues = new ArrayList<Double>();
	// 包括序号和计数
	private HashMap<Integer, Integer> hits = new HashMap<Integer, Integer>();
	// Only for debug.
	private LinkedList<Integer> hitIndices = new LinkedList<Integer>();

	public String getPath() {
		return path;
	}

	public void setPath(String value) {
		path = value;
	}

	public int getClassify() {
		return classify;
	}

	public void setClassify(int value) {
		classify = value;
	}

	public HashMap<Integer, Integer> getHits() {
		return hits;
	}
	
	public ArrayList<Double> getClassifyValues() {
		return classifyValues;
	}
	
	public void addClassifyValue(double value) {
		classifyValues.add(value);
	}

	public void InsertWord(Integer index) {
		hitIndices.push(index);
		if (hits.containsKey(index)) {
			Integer count = hits.remove(index);
			hits.put(index, ++count);
		} else {
			hits.put(index, 1);
		}
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

	/*
	 * protected HashMap<Integer, Integer> getWords() { return words; }
	 * 
	 * protected void setWords(HashMap<Integer, Integer> value) { words = value;
	 * }
	 */
}
