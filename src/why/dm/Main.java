/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.jfree.data.category.DefaultCategoryDataset;

import why.dm.knn.Knn;

/**
 * The Main class
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public final class Main {

	/**
	 * Entry function
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Date begin = new Date();

		/*
		 * // Plot //double[] x = randomNormal(100, 0, 1); // 1000 random
		 * numbers from a normal (Gaussian) statistical law //double[] y =
		 * randomUniform(1000, -3, 3); // 1000 random numbers from a uniform
		 * statistical law double[] x = {1, 3, 5, 10, 18}; double[] y = {2, 4,
		 * 6, 5, 8, 1}; //double[][] x = {{1,2},{3,1},{5,1}}; //double[][] y =
		 * {{7,1},{9,1},{11,1}}; Plot2DPanel plot = new Plot2DPanel();
		 * //plot.addLinePlot("my plot", x, y); //plot.addScatterPlot("my plot",
		 * x, y); //plot.addBarPlot("my plot", x, y);
		 * //plot.addBoxPlot("my plot", x, y);
		 * //plot.addHistogramPlot("Gaussian population", x, 10);
		 * //plot.addHistogramPlot("Uniform population", Color.RED, y, 50);
		 * plot.addStaircasePlot("my plot", x, y); JFrame frame = new
		 * JFrame("a plot panel"); frame.setSize(800, 600);
		 * frame.setContentPane(plot); frame.setVisible(true);
		 */

		// Extract feacture
		FeatureExtraction featureExtraction = new FeatureExtraction();
		System.out.println("Read Files:");
		featureExtraction.readFiles("runtime/newgroups");
		int totalTestPart = 10;
		featureExtraction.setTestProportion(1. / totalTestPart);
		NativeBayes nativeBayes = new NativeBayes();
		//BpAnn bpAnn = new BpAnn();
		Knn knn = new Knn();
		for (int testPart = 0; totalTestPart > testPart; ++testPart) {
			String testPartString = String.valueOf(testPart);
			System.out.println();
			System.out.println();
			System.out.println("Calculating round " + testPartString + "...");

			featureExtraction.selectTestDocuments(testPart);
			//System.out.println(featureExtraction.getTestDocuments().size());
			//System.out.println(featureExtraction.getTrainingFeature().getDocuments().size());
			//System.out.println(featureExtraction.getTestDocuments().get(0).getPath());
			//System.out.println();
			//System.out.println("Select Features:");
			featureExtraction.selectFeature();
			//System.out.println();
			//featureExtraction.traceTerm();

			// Naive Bayes classification
			System.out.println("Naive Bayes...");
			nativeBayes.clear();
			nativeBayes.setDebugFileName("native_bayes_" + testPartString);
			nativeBayes.setFeatureExtraction(featureExtraction);
			nativeBayes.train();
			nativeBayes.test();
	
			// BP ANN classification
			//System.out.println("BP ANN...");
			//bpAnn.clear();
			//bpAnn.setDebugFileName("bp_ann_" + testPartString);
			//bpAnn.setFeatureExtraction(featureExtraction);
			//bpAnn.train();
			//bpAnn.test();
	
			// KNN classification
			System.out.println("KNN...");
			knn.clear();
			knn.setDebugFileName("knn_" + testPartString);
			knn.setFeatureExtraction(featureExtraction);
			knn.train();
			knn.test();

		}

		Date end = new Date();
		/*
		 * // Chart DefaultCategoryDataset dataset =
		 * toDataset(featureExtraction); // --chart-------------- JFreeChart
		 * jfreechart = ChartFactory.createBarChart( "Naive Bayes Chart",
		 * "Classification", "Value", dataset, PlotOrientation.VERTICAL, false,
		 * true, false); jfreechart.setBackgroundPaint(Color.white);
		 * CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		 * categoryplot.setBackgroundPaint(new Color(238, 238, 255));
		 * categoryplot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT); //
		 * --------------------------------------- try { // Save image
		 * java.util.Date d = new java.util.Date(); String time =
		 * String.valueOf(d.getTime()); ChartUtilities.saveChartAsPNG(new
		 * File("runtime/naivebayes" + time + ".png"), jfreechart, 2048, 768);
		 * // ChartUtilities.saveChartAsJPEG(new File("trace/BarChart.jpg"), //
		 * chart, 368, 278); } catch (Exception e) { e.printStackTrace(); //
		 * System.err.println("Problem occurred creating chart!" // +
		 * e.getMessage()); }
		 */
		// Show time spent
		long l = end.getTime() - begin.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long minute = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long second = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
		System.out.println();
		System.out.println("Used time: " + day + "d " + hour + "h " + minute
				+ "m " + second + "s.");
	}

	public static DefaultCategoryDataset toDataset(
			FeatureExtraction featureExtraction) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int currentDocument = 0;
		Iterator<Document> iter = featureExtraction.getTestDocuments()
				.iterator();
		while (iter.hasNext()) {
			++currentDocument;
			Document document = iter.next();
			ArrayList<Double> values = document.getClassifyValues();
			for (int index = 0; featureExtraction.getTrainingFeature()
					.getClassifyHits().size() > index; ++index) {
				dataset.addValue(-values.get(index), String.valueOf(index),
						currentDocument + "" + index);// String.valueOf(currentRound++));
			}
		}
		return dataset;
	}

}
