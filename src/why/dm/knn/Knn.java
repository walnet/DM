/**
 * Copyright (C) 2012 why
 */
package why.dm.knn;

import why.dm.Classifier;

/**
 * 2012-3-22 下午4:34:06
 *
 * @author qinhuiwang
 * @version $Rev$ $Date$
 */
public class Knn extends Classifier{

	/*
	 * @see why.dm.Classifier#clear()
	 */
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * @see why.dm.Classifier#test()
	 */
	@Override
	public void test() {
		redirectToNewOutput("test_" + debugFileName + ".txt");
		ComputeAllDocuments.ComputeDistanceFromTestDocsToTrainDocs(this);
		redirectToOldOutput();
	}

	/*
	 * @see why.dm.Classifier#trace()
	 */
	@Override
	public void trace() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * @see why.dm.Classifier#train()
	 */
	@Override
	public void train() {
		redirectToNewOutput("train_" + debugFileName + ".txt");
		System.out.println("Nothing to train.");
		redirectToOldOutput();
	}

}
