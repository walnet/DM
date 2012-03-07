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
 * 本类的作用是遍历文件后来提取特征
 * 
 * @author qinhuiwang
 * @version $Rev$ $Date$
 */
public class FeatureExtraction {
	
	// 以下为所有文档共有（包括训练和测试）
	
	// 所有文档集合
	private ArrayList<LinkedList<Document>> allDocuments = new ArrayList<LinkedList<Document>>();
	// 所有term集合，包括名字和序号
	private HashMap<String, Integer> terms = new HashMap<String, Integer>();
	// Name string of all terms. Only for debug.
	private ArrayList<String> termIndices = new ArrayList<String>();
	// Classify name. Also first-layer sub folder name.
	private ArrayList<String> classifyNames = new ArrayList<String>();
	

	//Feature feature = new Feature();
	Feature trainingFeature = new Feature();
	//Feature testFeature = new Feature();

	// Index of termsIndices. Only for debug.
	private int currentTermIndex = 0;

	// 测试文档集合
	private LinkedList<Document> testDocuments = new LinkedList<Document>();
	// 测试文档比例（剩下的都是训练文档）
	private double testProportion = 0.01;

	// 打开调试
	private boolean debugTrace = false;
	private int debugCount = 0;

	public HashMap<String, Integer> getTerms() {
		return terms;
	}

	public ArrayList<String> getTermIndices() {
		return termIndices;
	}

	public ArrayList<String> getClassifyNames() {
		return classifyNames;
	}

	//public Feature getFeature() {
	//	return feature;
	//}

	public Feature getTrainingFeature() {
		return trainingFeature;
	}

	//public Feature getTestFeature() {
	//	return testFeature;
	//}

	public LinkedList<Document> getTestDocuments() {
		return testDocuments;
	}
	
	public void clear() {
		allDocuments.clear();
		terms.clear();
		termIndices.clear();
		trainingFeature.clear();
		currentTermIndex = 0;
		testDocuments.clear();
		classifyNames.clear();
	}

	/**
	 * 对指定目录下的文件进行提取特征
	 * 
	 * @param dirPath
	 */
	public void extractFeacture(String dirPath) {
		readTypesAndFilePaths(dirPath);
		doStem();
	}

	/**
	 * 递归获取目录中的文件
	 * 
	 * @param dirPath
	 * @param classify
	 */
	private void readDocumentList(String dirPath, int classify) {
		File dirFile = new File(dirPath);
		File[] files = dirFile.listFiles();

		if (null == files)
			return;

		for (int i = 0; files.length > i; ++i) {
			if (files[i].isDirectory())
				readDocumentList(files[i].getAbsolutePath(), classify);
			else {
				String fileNameStr = files[i].getAbsolutePath();
				// System.out.println(fNameStr);
				Document document = new Document();
				document.setPath(fileNameStr);
				document.setClassify(classify);
				allDocuments.get(classify).add(document);
				//documents().add(document);
				//classifyDocuments.get(classify).add(document);
			}
		}
	}

	/**
	 * 根据目录的路径，得到其目录下所有文件路径+名称，以及第一次子目录的文件
	 * 
	 * @param dirPath
	 */
	private void readTypesAndFilePaths(String dirPath) {
		File dirFile = new File(dirPath);
		File[] files = dirFile.listFiles();

		if (files == null)
			return;
		//documents.clear();
		clear();
		for (int i = 0; i < files.length; ++i) {
			if (files[i].isDirectory()) {
				allDocuments.add(new LinkedList<Document>());
				readDocumentList(files[i].getAbsolutePath(), i);
				classifyNames.add(files[i].getName());
			}
		}
	}

	/**
	 * 词法分析、去除停用词、用Porter Stemming Algorithm进行词根还原
	 */
	public void doStem() {
		char[] w = new char[501];
		Stemmer s = new Stemmer();
		s.readStopword();
		for (int classify = 0; allDocuments.size() > classify; ++classify) {

			Iterator<Document> iter = allDocuments.get(classify).iterator();
			while (iter.hasNext()) {
				Document currentDocument = iter.next();
				if (debugTrace) {
					System.out.print(currentDocument.getPath() + ": ");
				}
				try {
					FileInputStream in = new FileInputStream(
							currentDocument.getPath());
	
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

											// 加入全局索引
											Integer index = -1;
											if (null == (index = terms.get(u))) {
												terms.put(u, currentTermIndex);
												index = currentTermIndex;
												termIndices.add(u);
												++currentTermIndex;
											}
/*
											// 加入分类索引
											HashMap<Integer, Integer> currentClassifyHits = classifyHits
													.get(currentDocument
															.getClassify());
											if (currentClassifyHits
													.containsKey(index)) {
												Integer original = currentClassifyHits
														.get(index);
												currentClassifyHits.put(index,
														original + 1);
											} else {
												currentClassifyHits.put(index, 1);
											}
*/
											// 加入文档索引
											currentDocument.InsertWord(index);

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
						// System.out.println("error reading " +
						// currentDocument.getPath());
						e.printStackTrace();
						break;
					}
					// documents.push(currentDocument);
				} catch (FileNotFoundException e) {
					// System.out.println("file " + currentDocument.getPath() +
					// " not found");
					e.printStackTrace();
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
	}

	public void setTestProportion(double value) {
		testProportion = value;
	}

	public void selectTestDocuments() {
		// boolean testFull = false;
		testDocuments.clear();
		trainingFeature.clear();
		for (int classify = 0; allDocuments.size() > classify; ++classify) {
			trainingFeature.getClassifyHits().add(new HashMap<Integer, Integer>());
			trainingFeature.getClassifyDocuments().add(new LinkedList<Document>());
			int added = 0;
			LinkedList<Document> currentClassifyDocuments = allDocuments.get(classify);
			Iterator<Document> iterDocument = currentClassifyDocuments.iterator();
			Document currentDocument;
			while (iterDocument.hasNext()) {
				currentDocument = iterDocument.next();
				if ((double) added / (double) currentClassifyDocuments.size() <= testProportion) {
					testDocuments.add(currentDocument);
					++added;
				} else {
					trainingFeature.getClassifyDocuments().get(classify).add(currentDocument);
				}
				
				// 将项加入分类索引
				Iterator<Integer> iter = currentDocument.getHits().keySet().iterator();
				while (iter.hasNext()) {
					Integer currentKey = iter.next();
					Integer currentCount = currentDocument.getHits().get(currentKey);
					HashMap<Integer, Integer> currentClassifyHits = trainingFeature.getClassifyHits().get(classify);
					if (currentClassifyHits.containsKey(currentKey)) {
						Integer original = currentClassifyHits.get(currentKey);
						currentClassifyHits.put(currentKey, original + currentCount);
					} else {
						currentClassifyHits.put(currentKey, currentCount);
					}
				}

			}
		}
	}

	public void trace() {
		for (int classify = 0; allDocuments.size() > classify; ++classify) {
			LinkedList<Document> currentClassifyDocuments = allDocuments.get(classify);
			Iterator<Document> iterDocument = currentClassifyDocuments.iterator();
			while (iterDocument.hasNext()) {
				iterDocument.next().trace(classifyNames, termIndices);
			}
		}
	}

	/**
	 * @param args
	 */
	/*
	 * public static void main(String[] args) {
	 * getTypesAndFilePaths("bin/newgroups"); for (int i = 0; i <
	 * firstSubDirNames.size(); i++) {
	 * System.out.println(firstSubDirNames.get(i)); }
	 * System.out.println("Total files: "+filePaths.size()); }
	 */

}
