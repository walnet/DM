package ss.edu.pku.dm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ss.edu.pku.dm.ForeachFilesFromDirectory;

/**
 * 
 * @author qinhuiwang 本类的作用是遍历文件后来提取特征
 * 
 */
public class FeatureExtraction {
	private static List<String> filePaths; // 所有文件的路径+名称
	private static List<String> firstSubDirNames; // 第一层子文件夹的名称

	/**
	 * 必须先执行extractFeacture才有值，否则为null
	 * 
	 * @return the filePaths
	 */
	public static List<String> getFilePaths() {
		return filePaths;
	}

	/**
	 * 必须先执行extractFeacture才有值，否则为null
	 * 
	 * @return the firstSubDirNames
	 */
	public static List<String> getFirstSubDirNames() {
		return firstSubDirNames;
	}

	/**
	 * 对指定目录下的文件进行提取特征
	 * 
	 * @param dirPath
	 */
	public static void extractFeacture(String dirPath) {
		getTypesAndFilePaths(dirPath);
		Stemmer.doStem(filePaths);
	}

	// 根据目录的路径，得到其目录下所有文件路径+名称，以及第一次子目录的文件
	private static void getTypesAndFilePaths(String dirPath) {
		firstSubDirNames = new ArrayList<String>();
		filePaths = new ArrayList<String>();

		File dirFile = new File(dirPath);
		File[] files = dirFile.listFiles();

		if (files == null)
			return;
		ForeachFilesFromDirectory.clearColletion(); // 清空集合里面的所有元素
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				firstSubDirNames.add(files[i].getName());
				ForeachFilesFromDirectory.foreachFilesFromDirectory(files[i]
						.getAbsolutePath());
			}
		}
		filePaths = ForeachFilesFromDirectory.getFileNameList();

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
