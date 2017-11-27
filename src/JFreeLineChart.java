package Stock;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class JFreeLineChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	private static String inputFileName1;

	XYDataset inputData;
	JFreeChart chart;

	public JFreeLineChart(XYDataset inputFileName) throws IOException {
		super(inputFileName1);
		// Read sample data from prices.txt file
		
		inputData = inputFileName;
		// Create the chart using the sample data
		chart = createChart(inputData);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}


	private JFreeChart createChart(XYDataset inputData) throws IOException {
		// Create the chart using the data read from the prices.txt file
		JFreeChart chart = ChartFactory.createScatterPlot(
				"Stock Price", "Date", "Price", inputData,
				PlotOrientation.VERTICAL, true, true, false);

		XYPlot plot = chart.getXYPlot();
		plot.getRenderer().setSeriesPaint(0, Color.blue);
		return chart;
	}
}