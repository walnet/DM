package ss.edu.pku.dm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ForeachFilesFromDirectory {
	private static List<String> fileNameList = new ArrayList<String>();

	public static void clearColletion() {
		fileNameList.clear();
	}

	/**
	 * @return the fileNameList
	 */
	public static List<String> getFileNameList() {
		return fileNameList;
	}

	/**
	 * @param dirPath
	 * @return void
	 */
	public static void foreachFilesFromDirectory(String dirPath) {
		File dirFile = new File(dirPath);
		File[] files = dirFile.listFiles();

		if (files == null)
			return;

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				foreachFilesFromDirectory(files[i].getAbsolutePath());
			else {
				String fNameStr = files[i].getAbsolutePath();
				// System.out.println(fNameStr);
				fileNameList.add(fNameStr);
			}
		}
	}

	/**
	 * @param args
	 */
	/*
	 * public static void main(String[] args) { // TODO Auto-generated method
	 * stub String directoryString =
	 * "D:/qaddafi2008/�о���/��һ��/1_�����ھ�Ӧ��_0B922/�ĵ���-Newgroups 20"; long a =
	 * System.currentTimeMillis(); foreachFilesFromDirectory(directoryString);
	 * System.out.println(fileNameList.size());
	 * System.out.println("time:"+(System.currentTimeMillis()-a)); }
	 */

}
