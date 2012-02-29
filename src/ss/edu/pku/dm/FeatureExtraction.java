package ss.edu.pku.dm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ss.edu.pku.dm.ForeachFilesFromDirectory;

/**
 * 
 * @author qinhuiwang ����������Ǳ����ļ�������ȡ����
 * 
 */
public class FeatureExtraction {
	private static List<String> filePaths; // �����ļ���·��+����
	private static List<String> firstSubDirNames; // ��һ�����ļ��е�����

	/**
	 * ������ִ��extractFeacture����ֵ������Ϊnull
	 * 
	 * @return the filePaths
	 */
	public static List<String> getFilePaths() {
		return filePaths;
	}

	/**
	 * ������ִ��extractFeacture����ֵ������Ϊnull
	 * 
	 * @return the firstSubDirNames
	 */
	public static List<String> getFirstSubDirNames() {
		return firstSubDirNames;
	}

	/**
	 * ��ָ��Ŀ¼�µ��ļ�������ȡ����
	 * 
	 * @param dirPath
	 */
	public static void extractFeacture(String dirPath) {
		getTypesAndFilePaths(dirPath);
		Stemmer.doStem(filePaths);
	}

	// ����Ŀ¼��·�����õ���Ŀ¼�������ļ�·��+���ƣ��Լ���һ����Ŀ¼���ļ�
	private static void getTypesAndFilePaths(String dirPath) {
		firstSubDirNames = new ArrayList<String>();
		filePaths = new ArrayList<String>();

		File dirFile = new File(dirPath);
		File[] files = dirFile.listFiles();

		if (files == null)
			return;
		ForeachFilesFromDirectory.clearColletion(); // ��ռ������������Ԫ��
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
