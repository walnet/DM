/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @version $Rev$ $Date$
 * @abstract ����������Ǳ����ļ�������ȡ����
 * @author qinhuiwang
 */
public class FeatureExtraction {
	// ���Լ��ϣ��������ֺ����
	private HashMap<String, Integer> wordHashs = new HashMap<String, Integer>();
	// Name String of the attributes. Only for debug.
	private ArrayList<String> wordNames = new ArrayList<String>();
	// Index of wordNames. Only for debug.
	private int currentWordNamesIndex = 0;
	// �ĵ���������
	private LinkedList<Document> documents = new LinkedList<Document>();
	// ��������Ҳ�ǵ�һ�����ļ��е�����
	private ArrayList<String> classifyNames = new ArrayList<String>();
	private boolean debugTrace = true;
	private int debugCount = 0;

	/**
	 * ��ָ��Ŀ¼�µ��ļ�������ȡ����
	 * 
	 * @param dirPath
	 * @return void
	 */
	public void extractFeacture(String dirPath) {
		getTypesAndFilePaths(dirPath);
		doStem();
	}

	/**
	 * �ݹ��ȡĿ¼�е��ļ�
	 * 
	 * @param dirPath
	 * @param classify
	 * @return void
	 */
	private void getDocumentList(String dirPath, int classify) {
		File dirFile = new File(dirPath);
		File[] files = dirFile.listFiles();

		if (files == null)
			return;

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				getDocumentList(files[i].getAbsolutePath(), classify);
			else {
				String fileNameStr = files[i].getAbsolutePath();
				// System.out.println(fNameStr);
				Document document = new Document();
				document.setPath(fileNameStr);
				document.setClassify(classify);
				documents.add(document);
			}
		}
	}

	/**
	 * ����Ŀ¼��·�����õ���Ŀ¼�������ļ�·��+���ƣ��Լ���һ����Ŀ¼���ļ�
	 * 
	 * @param dirPath
	 * @return void
	 */
	private void getTypesAndFilePaths(String dirPath) {
		File dirFile = new File(dirPath);
		File[] files = dirFile.listFiles();

		if (files == null)
			return;
		documents.clear();
		for (int i = 0; i < files.length; ++i) {
			if (files[i].isDirectory()) {
				classifyNames.add(files[i].getName());
				getDocumentList(files[i].getAbsolutePath(), i);
			}
		}
	}

	/**
	 * �ʷ�������ȥ��ͣ�ôʡ���Porter Stemming Algorithm���дʸ���ԭ
	 * 
	 * @param void
	 * @return void
	 */
	public void doStem() {
		char[] w = new char[501];
		Stemmer s = new Stemmer();
		s.readStopword();
		Iterator<Document> iter = documents.iterator();
		while (iter.hasNext()) {
			Document document = iter.next();
			if (debugTrace) {
				System.out.print(document.getPath() + ": ");
			}
			try {
				FileInputStream in = new FileInputStream(document.getPath());

				try {
					while (true)

					{
						int ch = in.read();
						if (Character.isLetter((char) ch)) {
							int j = 0;
							while (true) {
								ch = Character.toLowerCase((char) ch);
								w[j] = (char) ch;
								if (j < 500)
									++j;
								ch = in.read();
								if (!Character.isLetter((char) ch)) {
									/* to test add(char ch) */
									for (int c = 0; c < j; c++)
										s.add(w[c]);

									/* or, to test add(char[] w, int j) */
									/* s.add(w, j); */

									if (s.isStopword()) {
										s.resetIndex();
									} else {
										s.stem();

										String u;

										/* and now, to test toString() : */
										u = s.toString();

										/*
										 * to test getResultBuffer(),
										 * getResultLength() :
										 */
										/*
										 * u = new String(s.getResultBuffer(),
										 * 0, s.getResultLength());
										 */

										if (debugTrace) {
											System.out.print(u);
										}
										Integer index = -1;
										if (null == (index = wordHashs.get(u))) {
											wordHashs.put(u, currentWordNamesIndex);
											index = currentWordNamesIndex;
											wordNames.add(u);
											++currentWordNamesIndex;
										}
										document.InsertWord(index);
									}
									break;
								}
							}
						}
						if (ch < 0)
							break;
						// System.out.print((char) ch);
						if (debugTrace) {
							System.out.print(',');
						}
					}
				} catch (IOException e) {
					System.out.println("error reading " + document.getPath());
					break;
				}
				//documents.push(currentDocument);
			} catch (FileNotFoundException e) {
				System.out.println("file " + document.getPath() + " not found");
				break;
			}
			if (debugTrace) {
				System.out.println('.');
			} else {
				++debugCount;
				if (0 == debugCount % 100) {
					System.out.print(String.valueOf(debugCount) + " ");
				}
				if (0 == debugCount % 2000) {
					System.out.println();
				}
			}
		}
	}
	
	public void trace() {
		for (int i = 0; documents.size() > i; ++i) {
			documents.get(i).trace(classifyNames, wordNames);
		}
	}

	/**
	 * @param args
	 * @return void
	 */
	/*
	 * public static void main(String[] args) {
	 * getTypesAndFilePaths("bin/newgroups"); for (int i = 0; i <
	 * firstSubDirNames.size(); i++) {
	 * System.out.println(firstSubDirNames.get(i)); }
	 * System.out.println("Total files: "+filePaths.size()); }
	 */

}
