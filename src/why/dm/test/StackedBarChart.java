/**
 * Copyright (C) 2012 why
 */
package why.dm.test;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

/**
 * Mar 23, 2012 9:03:27 AM
 * 
 * @author hector
 * @version $Rev$ $Date$
 */
public final class StackedBarChart {

	private static final String CHART_PATH = "test/";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StackedBarChart stackedBarChart = new StackedBarChart();
		stackedBarChart.makeStackedBarChart();
	}

	public String createStackedBarChart(CategoryDataset dataset, String xName,
			String yName, String chartTitle, String charName) {
		// 1:得到 CategoryDataset

		// 2:JFreeChart对象
		JFreeChart chart = ChartFactory.createStackedBarChart(chartTitle, // 图表标题
				xName, // 目录轴的显示标签
				yName, // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				true, // 是否显示图例(对于简单的柱状图必须是false)
				false, // 是否生成工具
				false // 是否生成URL链接
				);
		// 图例字体清晰
		chart.setTextAntiAlias(false);

		chart.setBackgroundPaint(Color.WHITE);

		// 2 ．2 主标题对象 主标题对象是 TextTitle 类型
		chart.setTitle(new TextTitle(chartTitle, new Font("隶书", Font.BOLD, 25)));
		// 2 ．2.1:设置中文
		// x,y轴坐标字体
		Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);

		// 2 ．3 Plot 对象 Plot 对象是图形的绘制结构对象
		CategoryPlot plot = chart.getCategoryPlot();

		// 设置横虚线可见
		plot.setRangeGridlinesVisible(true);
		// 虚线色彩
		plot.setRangeGridlinePaint(Color.gray);

		// 数据轴精度
		NumberAxis vn = (NumberAxis) plot.getRangeAxis();
		// 设置最大值是1
		vn.setUpperBound(1);
		// 设置数据轴坐标从0开始
		// vn.setAutoRangeIncludesZero(true);
		// 数据显示格式是百分比
		DecimalFormat df = new DecimalFormat("0.00%");
		vn.setNumberFormatOverride(df); // 数据轴数据标签的显示格式
		// DomainAxis （区域轴，相当于 x 轴）， RangeAxis （范围轴，相当于 y 轴）

		CategoryAxis domainAxis = plot.getDomainAxis();

		domainAxis.setLabelFont(labelFont);// 轴标题

		domainAxis.setTickLabelFont(labelFont);// 轴数值

		// x轴坐标太长，建议设置倾斜，如下两种方式选其一，两种效果相同
		// 倾斜（1）横轴上的 Lable 45度倾斜
		// domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		// 倾斜（2）Lable（Math.PI 3.0）度倾斜
		// domainAxis.setCategoryLabelPositions(CategoryLabelPositions
		// .createUpRotationLabelPositions(Math.PI / 3.0));

		domainAxis.setMaximumCategoryLabelWidthRatio(0.6f);// 横轴上的 Lable 是否完整显示

		plot.setDomainAxis(domainAxis);

		// y轴设置
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(labelFont);
		rangeAxis.setTickLabelFont(labelFont);
		// 设置最高的一个 Item 与图片顶端的距离
		rangeAxis.setUpperMargin(0.15);
		// 设置最低的一个 Item 与图片底端的距离
		rangeAxis.setLowerMargin(0.15);
		plot.setRangeAxis(rangeAxis);

		// Renderer 对象是图形的绘制单元
		StackedBarRenderer renderer = new StackedBarRenderer();
		// 设置柱子宽度
		renderer.setMaximumBarWidth(0.05);
		// 设置柱子高度
		renderer.setMinimumBarLength(0.1);
		// 设置柱的边框颜色
		renderer.setBaseOutlinePaint(Color.BLACK);
		// 设置柱的边框可见
		renderer.setDrawBarOutline(true);

		// // 设置柱的颜色(可设定也可默认)
		renderer.setSeriesPaint(0, new Color(204, 255, 204));
		renderer.setSeriesPaint(1, new Color(255, 204, 153));

		// 设置每个地区所包含的平行柱的之间距离
		renderer.setItemMargin(0.4);

		plot.setRenderer(renderer);
		// 设置柱的透明度(如果是3D的必须设置才能达到立体效果，如果是2D的设置则使颜色变淡)
		// plot.setForegroundAlpha(0.65f);

		FileOutputStream fos_jpg = null;
		try {
			isChartPathExist(CHART_PATH);
			String chartName = CHART_PATH + charName;
			fos_jpg = new FileOutputStream(chartName);
			ChartUtilities.writeChartAsPNG(fos_jpg, chart, 500, 500, true, 10);
			return chartName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fos_jpg.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 柱状图,折线图 数据集
	public CategoryDataset getBarData(double[][] data, String[] rowKeys,
			String[] columnKeys) {
		return DatasetUtilities
				.createCategoryDataset(rowKeys, columnKeys, data);

	}

	/**
	 * 判断文件夹是否存在，如果不存在则新建
	 * 
	 * @param chartPath
	 */
	private void isChartPathExist(String chartPath) {
		File file = new File(chartPath);
		if (!file.exists()) {
			file.mkdirs();
			// log.info("CHART_PATH="+CHART_PATH+"create.");
		}
	}

	/**
	 * 生成堆栈柱状图
	 */
	public void makeStackedBarChart() {
		double[][] data = new double[][] { { 0.21, 0.66, 0.23, 0.40, 0.26 },
				{ 0.25, 0.21, 0.10, 0.40, 0.16 } };
		String[] rowKeys = { "苹果", "梨子" };
		String[] columnKeys = { "北京", "上海", "广州", "成都", "深圳" };
		CategoryDataset dataset = getBarData(data, rowKeys, columnKeys);
		createStackedBarChart(dataset, "x坐标", "y坐标", "柱状图", "stsckedBar.png");
	}

}
