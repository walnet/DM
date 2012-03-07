/**
 * 
 */
package why.dm.test;

import java.awt.Color;
import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.block.EmptyBlock;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

/**
 * @author Hector
 * 
 */
public final class JFCTest {

	/**
	 * @param args
	 *//*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getpic4();
	}*/

	public static void getpic4() {

		String s = "S1";
		String s1 = "S2";
		String s2 = "S3";

		String s3 = "Cate 1";
		String s4 = "Cate 2";
		String s5 = "Cate 3";
		String s6 = "Cate 4";
		String s7 = "Cate 5";
		String s8 = "Cate 6";
		String s9 = "Cate 7";
		String s10 = "Cate 8";

		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		defaultcategorydataset.addValue(1.0D, s, s3);
		defaultcategorydataset.addValue(4D, s, s4);
		defaultcategorydataset.addValue(3D, s, s5);
		defaultcategorydataset.addValue(5D, s, s6);
		defaultcategorydataset.addValue(5D, s, s7);
		defaultcategorydataset.addValue(7D, s, s8);
		defaultcategorydataset.addValue(7D, s, s9);
		defaultcategorydataset.addValue(8D, s, s10);
		defaultcategorydataset.addValue(5D, s1, s3);
		defaultcategorydataset.addValue(7D, s1, s4);
		defaultcategorydataset.addValue(6D, s1, s5);
		defaultcategorydataset.addValue(8D, s1, s6);
		defaultcategorydataset.addValue(4D, s1, s7);
		defaultcategorydataset.addValue(4D, s1, s8);
		defaultcategorydataset.addValue(2D, s1, s9);
		defaultcategorydataset.addValue(1.0D, s1, s10);
		defaultcategorydataset.addValue(4D, s2, s3);
		defaultcategorydataset.addValue(3D, s2, s4);
		defaultcategorydataset.addValue(2D, s2, s5);
		defaultcategorydataset.addValue(3D, s2, s6);
		defaultcategorydataset.addValue(6D, s2, s7);
		defaultcategorydataset.addValue(3D, s2, s8);
		defaultcategorydataset.addValue(4D, s2, s9);
		defaultcategorydataset.addValue(3D, s2, s10);

		// --chart--------------
		JFreeChart jfreechart = ChartFactory.createBarChart("Dual Axis Chart",
				"Category", "Value", defaultcategorydataset,
				PlotOrientation.VERTICAL, false, true, false);
		jfreechart.setBackgroundPaint(Color.white);
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		categoryplot.setBackgroundPaint(new Color(238, 238, 255));
		categoryplot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
		// ---------------------------------------

		DefaultCategoryDataset defaultcategorydataset2 = new DefaultCategoryDataset();
		defaultcategorydataset2.addValue(15D, "S4", "Cate1");
		defaultcategorydataset2.addValue(24D, "S4", "Cate2");
		defaultcategorydataset2.addValue(31D, "S4", "Cate3");
		defaultcategorydataset2.addValue(25D, "S4", "Cate4");
		defaultcategorydataset2.addValue(56D, "S4", "Cate5");
		defaultcategorydataset2.addValue(37D, "S4", "Cate6");
		defaultcategorydataset2.addValue(77D, "S4", "Cate7");
		defaultcategorydataset2.addValue(18D, "S4", "Cate8");

		categoryplot.setDataset(1, defaultcategorydataset2);
		categoryplot.mapDatasetToRangeAxis(1, 1);
		CategoryAxis categoryaxis = categoryplot.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		NumberAxis numberaxis = new NumberAxis("Secondary");
		categoryplot.setRangeAxis(1, numberaxis);

		LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
		lineandshaperenderer
				.setToolTipGenerator(new StandardCategoryToolTipGenerator());
		categoryplot.setRenderer(1, lineandshaperenderer);
		categoryplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		LegendTitle legendtitle = new LegendTitle(categoryplot.getRenderer(0));
		legendtitle.setMargin(new RectangleInsets(2D, 2D, 2D, 2D));
		legendtitle.setBorder(new BlockBorder());
		LegendTitle legendtitle1 = new LegendTitle(categoryplot.getRenderer(1));
		legendtitle1.setMargin(new RectangleInsets(2D, 2D, 2D, 2D));
		legendtitle1.setBorder(new BlockBorder());
		BlockContainer blockcontainer = new BlockContainer(
				new BorderArrangement());
		blockcontainer.add(legendtitle, RectangleEdge.LEFT);
		blockcontainer.add(legendtitle1, RectangleEdge.RIGHT);
		blockcontainer.add(new EmptyBlock(2000D, 0.0D));
		CompositeTitle compositetitle = new CompositeTitle(blockcontainer);
		compositetitle.setPosition(RectangleEdge.BOTTOM);
		jfreechart.addSubtitle(compositetitle);
		try {
			// 保存圖片到指定文件夾
			java.util.Date d = new java.util.Date();
			String filename = String.valueOf(d.getTime());
			ChartUtilities
					.saveChartAsJPEG(
							new File(
									"E://BarChart4.jpg"),
							jfreechart, 1368, 278);
			// ChartUtilities.saveChartAsJPEG(new File("F://BarChart.jpg"),
			// chart, 368, 278);
		} catch (Exception e) {
			System.err.println("Problem occurred creating chart!"
					+ e.getMessage());
		}
	}

}
