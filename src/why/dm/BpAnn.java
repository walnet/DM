/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The BP ANN classifier class
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public class BpAnn extends Classifier {

	// 收敛速度
	private double eta = 0.5;
	// 隐藏层和输出层（这个数组没啥用，只是引用）
	private ArrayList<ArrayList<SigmoidUnit>> layers = new ArrayList<ArrayList<SigmoidUnit>>();
	// 输出层
	private ArrayList<SigmoidUnit> outputLayer = new ArrayList<SigmoidUnit>();
	// 隐藏层
	private ArrayList<SigmoidUnit> hiddenLayer = new ArrayList<SigmoidUnit>();
	// 缓存隐藏层的输出结果
	private ArrayList<Double> hiddenResult = new ArrayList<Double>();

	private void adjustHidden(int target, ArrayList<Double> inputs, int index) {
		// For each network unit, calculate its error term omega
		double omega;
		double result = hiddenLayer.get(index).getResult();
		double sum = 0.;
		for (int i = 0; outputLayer.size() > i; ++i) {
			sum += outputLayer.get(i).getWs().get(index)
					* outputLayer.get(i).getErrorTerm();
		}
		omega = result * (1 - result) * sum;
		hiddenLayer.get(index).setErrorTerm(omega);

		// Update each network weight w
		ArrayList<Double> ws = hiddenLayer.get(index).getWs();
		for (int i = 0; ws.size() > i; ++i) {
			double w = ws.get(i);
			double d = inputs.get(i);
			if (0. != d) {
				ws.set(i, w + eta * omega * d);
			}
		}
	}

	private void adjustOutput(int target, int index) {
		// For each network unit, calculate its error term omega
		double omega;
		double result = outputLayer.get(index).getResult();
		double t;
		if (target == index) {
			t = 1;
		} else {
			t = 0;
		}
		omega = result * (1 - result) * (t - result);
		outputLayer.get(index).setErrorTerm(omega);

		// Update each network weight w
		ArrayList<Double> ws = outputLayer.get(index).getWs();
		for (int i = 0; ws.size() > i; ++i) {
			double w = ws.get(i);
			double delta = eta * omega * hiddenResult.get(i);
			ws.set(i, w + delta);
		}
	}

	private void calculate(ArrayList<Double> inputs) {
		hiddenResult.clear();
		for (int i = 0; hiddenLayer.size() > i; ++i) {
			hiddenResult.add(hiddenLayer.get(i).calculateOutput(inputs));
		}
		for (int i = 0; outputLayer.size() > i; ++i) {
			outputLayer.get(i).calculateOutput(hiddenResult);
		}
	}

	private int calculateTotalHidden(int totalInput, int totalOutput) {
		return (int) Math.ceil(Math.log(totalInput));
	}

	/*
	 * @see why.dm.Classifier#clear()
	 */
	@Override
	public void clear() {
		layers.clear();
		outputLayer.clear();
		hiddenLayer.clear();
		hiddenResult.clear();
	}

	private void getCloneWs(ArrayList<ArrayList<Double>> outputResult,
			ArrayList<ArrayList<Double>> hiddenResult) {
		outputResult.clear();
		hiddenResult.clear();
		for (int i = 0; outputLayer.size() > i; ++i) {
			ArrayList<Double> outputLayerList = new ArrayList<Double>(
					outputLayer.get(i).getWs());
			outputResult.add(outputLayerList);
		}
		for (int i = 0; hiddenLayer.size() > i; ++i) {
			ArrayList<Double> hiddenLayerList = new ArrayList<Double>(
					hiddenLayer.get(i).getWs());
			hiddenResult.add(hiddenLayerList);
		}
	}

	/**
	 * @return the eta
	 */
	public double getEta() {
		return eta;
	}

	private void init(int totalInput, int totalOutput) {
		int totalHidden = calculateTotalHidden(totalInput, totalOutput);
		layers.add(hiddenLayer);
		layers.add(outputLayer);

		// TODO 这里可以考虑是用new还是clone
		for (int i = 0; totalHidden > i; ++i) {
			hiddenLayer.add(new SigmoidUnit(totalInput));
		}
		for (int i = 0; totalOutput > i; ++i) {
			outputLayer.add(new SigmoidUnit(totalHidden));
		}
	}

	/**
	 * @param eta
	 *            the eta to set
	 */
	public void setEta(double eta) {
		this.eta = eta;
	}

	/*
	 * @see why.dm.Classifier#test()
	 */
	@Override
	public void test() {
		redirectToNewOutput("test_" + debugFileName + ".txt");
		ArrayList<Integer> selectedFeatures = featureExtraction
				.getSelectedFeatures();
		int totalSelectedFeature = selectedFeatures.size();
		// Only for debug
		int currentDocument = 0;
		if (debugTrace) {
			initResultMatrix(); // 初始化结果矩阵
		}
		Iterator<Document> iterDocument = featureExtraction.getTestDocuments()
				.iterator();
		while (iterDocument.hasNext()) {

			// Test
			Document testDocument = iterDocument.next();
			ArrayList<Double> inputs = new ArrayList<Double>(
					totalSelectedFeature);
			for (int i = 0; totalSelectedFeature > i; ++i) {
				inputs.add(0.);
			}
			HashMap<Integer, Integer> hits = testDocument.getHits();
			Iterator<Integer> iterKey = hits.keySet().iterator();
			while (iterKey.hasNext()) {
				Integer key = iterKey.next();
				int index = selectedFeatures.indexOf(key);
				if (-1 != index) {
					inputs.set(index, hits.get(key).doubleValue());
				}
			}
			int classify = testInput(inputs);
			if (debugTrace) {
				int lastIndex = testDocument.getPath().lastIndexOf('\\');
				int lastIndex2 = testDocument.getPath().lastIndexOf('/');
				if (lastIndex2 > lastIndex)
					lastIndex = lastIndex2;
				System.out.println("(" + testDocument.getClassify() + ") "
						+ testDocument.getPath().substring(lastIndex + 1)
						+ ">> " + classify);
				accumulateResultMatrix(classify, testDocument.getClassify()); // 计算结果矩阵个元素值，用于矩阵输出
			}
			++currentDocument;
			testDocument.setGuessClassify(classify);
		}
		if (debugTrace) {
			calculateResultMatrix(); // 计算比例
			outputResultMaxtrix(); // 输出结果矩阵
		} else {
			System.out.println("Finished!");
		}
		redirectToOldOutput();
	}

	private int testInput(ArrayList<Double> inputs) {
		// Calculate output
		calculate(inputs);
		// Test result. Find min difference to 1.
		double minDifference = Double.POSITIVE_INFINITY;
		int minClassify = -1;
		for (int classify = 0; outputLayer.size() > classify; ++classify) {
			double result = outputLayer.get(classify).getResult();
			double difference = Math.abs(result - 1);
			if (minDifference > difference) {
				minDifference = difference;
				minClassify = classify;
			}
		}
		return 0;
	}

	/*
	 * @see why.dm.Classifier#trace()
	 */
	@Override
	public void trace() {
		for (int i = 0; outputLayer.size() > i; ++i) {
			System.out.print(i + ": ");
			outputLayer.get(i).trace();
			System.out.println();
		}
		for (int i = 0; hiddenLayer.size() > i; ++i) {
			System.out.print(i + ": ");
			hiddenLayer.get(i).trace();
			System.out.println();
		}
	}

	/*
	 * @see why.dm.Classifier#train()
	 */
	@Override
	public void train() {
		redirectToNewOutput("train_" + debugFileName + ".txt");
		ArrayList<Integer> selectedFeatures = featureExtraction
				.getSelectedFeatures();
		int totalSelectedFeature = selectedFeatures.size();
		int totalClassify = featureExtraction.getTrainingFeature()
				.getClassifyDocuments().size();
		init(totalSelectedFeature, totalClassify);
		// Only for debug
		ArrayList<ArrayList<Double>> lastOutputResult = null, currentOutputResult = null;
		ArrayList<ArrayList<Double>> lastHiddenResult = null, currentHiddenResult = null;
		// 循环训练10次
		for (int loop = 0; 10 > loop; ++loop) {
			if (debugTrace) {
				System.out.println();
				System.out.println(loop + " >>>");
			}
			trace();
			// Only for debug
			int currentDocument = 0;
			Iterator<Document> iterDocument = featureExtraction
					.getTrainingFeature().getDocuments().iterator();
			while (iterDocument.hasNext()) {
				// Only for debug
				lastOutputResult = currentOutputResult;
				lastHiddenResult = currentHiddenResult;
				currentOutputResult = new ArrayList<ArrayList<Double>>();
				currentHiddenResult = new ArrayList<ArrayList<Double>>();
				getCloneWs(currentOutputResult, currentHiddenResult);
				if (null != currentOutputResult
						&& null != lastOutputResult
						&& 0 >= currentOutputResult.get(0).get(0)
								* lastOutputResult.get(0).get(0)) {
					// TODO Only for debug
					int i = 0;
					++i;
				}
				if (0 > currentOutputResult.get(19).get(0)) {
					// TODO Only for debug
					int i = 0;
					++i;
				}

				// Train
				Document trainingDocument = iterDocument.next();
				ArrayList<Double> inputs = new ArrayList<Double>(
						totalSelectedFeature);
				for (int i = 0; totalSelectedFeature > i; ++i) {
					inputs.add(0.);
				}
				HashMap<Integer, Integer> hits = trainingDocument.getHits();
				Iterator<Integer> keyIterator = hits.keySet().iterator();
				while (keyIterator.hasNext()) {
					Integer key = keyIterator.next();
					int index = selectedFeatures.indexOf(key);
					if (-1 != index) {
						inputs.set(index, hits.get(key).doubleValue());
					}
				}
				trainInput(trainingDocument.getClassify(), inputs);
				++currentDocument;
			}
		}
		trace();
		redirectToOldOutput();
	}

	private void trainInput(int target, ArrayList<Double> inputs) {
		// Calculate output
		calculate(inputs);
		// Back propagation
		for (int i = 0; outputLayer.size() > i; ++i) {
			adjustOutput(target, i);
		}
		for (int i = 0; hiddenLayer.size() > i; ++i) {
			adjustHidden(target, inputs, i);
		}
	}

}
