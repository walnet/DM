/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @version $Rev$ $Date$
 * @abstract The Document class.
 * @author hector
 */
public class Document {
	// 文件路径
	private String path;
	// 所属分类
	private int classify = -1;
	// 包括序号和计数
	private HashMap<Integer, Integer> words = new HashMap<Integer, Integer>();
	private LinkedList<Integer> wordIndices = new LinkedList<Integer>();
	
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
	
	public void InsertWord(Integer index) {
		wordIndices.push(index);
		if (words.containsKey(index)) {
			Integer count = words.remove(index);
			words.put(index, ++count);
		} else {
			words.put(index, 1);
		}
	}
	
	public void trace() {
		System.out.print("Class " + String.valueOf(classify) + ">> ");
		Iterator<Integer> iter = wordIndices.descendingIterator();
		while (iter.hasNext()) {
			Integer index = iter.next();
			System.out.print(index + ": " + words.get(index) + "; ");
		}
		System.out.println();
	}
	
	public void trace(ArrayList<String> firstSubDirNames, ArrayList<String> wordNames) {
		String str = firstSubDirNames.get(classify);
		System.out.print(str + "(" + String.valueOf(classify) + ") " + path.substring(path.lastIndexOf('\\') + 1) + ">> ");
		Iterator<Integer> iter = wordIndices.descendingIterator();
		while (iter.hasNext()) {
			Integer index = iter.next();
			System.out.print(index + "(" + wordNames.get(index) + "): " + words.get(index) + "; ");
		}
		System.out.println();
	}
	
/*
	protected HashMap<Integer, Integer> getWords() {
		return words;
	}
	
	protected void setWords(HashMap<Integer, Integer> value) {
		words = value;
	}
*/
}
