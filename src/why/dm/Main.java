/**
 * Copyright (C) 2012 why
 */
package why.dm;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

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
		featureExtraction.extractFeacture("bin/newgroups");
		featureExtraction.setTestProportion(0.1);
		featureExtraction.selectTestDocuments();
		//System.out.println();
		//featureExtraction.trace();

		// Naive Bayes classification
		System.out.println();
		NativeBayes nativeBayes = new NativeBayes();
		nativeBayes.setFeatureExtraction(featureExtraction);
		nativeBayes.getPdc(new Document(), 2);
		nativeBayes.classify();
		Date end = new Date();

		// Chart
		DefaultCategoryDataset dataset = toDataset(featureExtraction);
		// --chart--------------
		JFreeChart jfreechart = ChartFactory.createBarChart(
				"Naive Bayes Chart", "Classification", "Value", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		jfreechart.setBackgroundPaint(Color.white);
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		categoryplot.setBackgroundPaint(new Color(238, 238, 255));
		categoryplot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
		// ---------------------------------------
		try {
			// Save image
			java.util.Date d = new java.util.Date();
			String time = String.valueOf(d.getTime());
			ChartUtilities.saveChartAsPNG(new File("E://naivebayes" + time
					+ ".png"), jfreechart, 2048, 768);
			// ChartUtilities.saveChartAsJPEG(new File("F://BarChart.jpg"),
			// chart, 368, 278);
		} catch (Exception e) {
			e.printStackTrace();
			//System.err.println("Problem occurred creating chart!"
					//+ e.getMessage());
		}

		// Show time spent
		long l = end.getTime() - begin.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long minute = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long second = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
		System.out.println("Used time: " + day + "d " + hour + "h " + minute
				+ "m " + second + "s.");
	}

	public static DefaultCategoryDataset toDataset(FeatureExtraction featureExtraction) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			int currentDocument = 0;
			Iterator<Document> iter = featureExtraction.getTestDocuments()
					.iterator();
			while (iter.hasNext()) {
				++currentDocument;
				Document document = iter.next();
				ArrayList<Double> values = document.getClassifyValues();
		for (int index = 0; featureExtraction.getTrainingFeature().getClassifyHits().size() > index; ++index) {
					dataset.addValue(-values.get(index), String.valueOf(index), currentDocument + "" + index);//String.valueOf(currentRound++));
			}
		}
		return dataset;
	}

}
