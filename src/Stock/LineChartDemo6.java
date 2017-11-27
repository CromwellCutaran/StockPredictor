package Stock;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
//import org.jfree.ui.Spacer;

import com.google.protobuf.TextFormat.ParseException;

/**
 * A simple demonstration application showing how to create a line chart using data from an
 * {@link XYDataset}.
 *
 */
public class LineChartDemo6 extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     * @throws FileNotFoundException 
     */

	XYDataset inputData;
	JFreeChart chart;
    public LineChartDemo6(final String title) throws FileNotFoundException, java.text.ParseException {

        super(title);
        
        final XYDataset dataset = createDataset();
        chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        
    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     * @throws FileNotFoundException 
     * @throws java.text.ParseException 
     */
    private XYDataset createDataset() throws FileNotFoundException, java.text.ParseException {
    	
    	Scanner scanner = new Scanner(new File("/Users/Grewal/Desktop/text.txt"));

        //final XYSeries series1 = new XYSeries("Second");
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries series1 = new TimeSeries("Second");
        

    	// Read the price and the living area
        int i = 0;
    	while (i < 2) {
    		if (scanner.hasNextFloat()) {
    			double livingArea = scanner.nextDouble();
    			String price = scanner.next();
    			 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    		        String dateInString = price;

    		        java.util.Date date = formatter.parse(dateInString);
					System.out.println(date);
					System.out.println(formatter.format(date));
    			if(price.charAt(0) == '0') {
    				price = price.substring(1);
    			}
    			double p = Double.parseDouble(price);
    			System.out.println(livingArea + " " +  p);
    			series1.add(new Day(date), livingArea);
    			i++;
    		}
    	}
    	scanner.close();
    	
    	 
         
         
         dataset = new TimeSeriesCollection();
        dataset.addSeries(series1);
        inputData = dataset;
        return dataset;
        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Line Chart Demo 6",      // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }
    
	private void drawRegressionLine() {
		// Get the parameters 'a' and 'b' for an equation y = a + b * x,
		// fitted to the inputData using ordinary least squares regression.
		// a - regressionParameters[0], b - regressionParameters[1]
		double regressionParameters[] = Regression.getOLSRegression(inputData,
				0);

		// Prepare a line function using the found parameters
		LineFunction2D linefunction2d = new LineFunction2D(
				regressionParameters[0], regressionParameters[1]);

		// Creates a dataset by taking sample values from the line function
		XYDataset dataset = DatasetUtilities.sampleFunction2D(linefunction2d,
				800D, 950, 100, "Fitted Regression Line");

		// Draw the line dataset
		XYPlot xyplot = chart.getXYPlot();
		xyplot.setDataset(1, dataset);
		XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(
				true, false);
		xylineandshaperenderer.setSeriesPaint(0, Color.YELLOW);
		xyplot.setRenderer(1, xylineandshaperenderer);
	}

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     * @throws FileNotFoundException 
     * @throws java.text.ParseException 
     * @throws java.text.ParseException 
     */
	 public static void main(String args[]) throws FileNotFoundException, java.text.ParseException{

	        DefaultKeyedValues data = new DefaultKeyedValues();
	        
	     	Scanner scanner = new Scanner(new File("/Users/Grewal/Desktop/text.txt"));

	        int i = 0;
	    	while (i < 10) {
	    		if (scanner.hasNextFloat()) {
	    			double livingArea = scanner.nextDouble();
	    			String price = scanner.next();
	    			 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    		        String dateInString = price;

	    		        java.util.Date date = formatter.parse(dateInString);
						//System.out.println(date);
						System.out.println(formatter.format(date) + " "+ livingArea);
	    			if(price.charAt(0) == '0') {
	    				price = price.substring(1);
	    			}
	    			//double p = Double.parseDouble(price);
	    			//System.out.println(livingArea + " " +  p);
	    			data.addValue(formatter.format(date), livingArea);
	    			i++;
	    		}
	    	}
	    	scanner.close();
	    	
	        
	        
	        
	        
	        //data.addValue("8/4/2012" ,7.0);
	        //data.addValue("19/04/2012",5.0);

	        CategoryDataset dataset = DatasetUtilities.createCategoryDataset("Stock", data);
	        JFreeChart chart = ChartFactory.createBarChart("Stock","Date","OBV",dataset,PlotOrientation.VERTICAL,true,true,true);
	        ChartFrame frame = new ChartFrame("Test", chart);

	        //Switch from a Bar Rendered to a LineAndShapeRenderer so the chart looks like an XYChart
	        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
	        renderer.setBaseLinesVisible(false); //TUrn of the lines
	        CategoryPlot plot = (CategoryPlot) chart.getPlot();
	        plot.setRenderer(0, renderer);

	        NumberAxis numberAxis = (NumberAxis)plot.getRangeAxis();        
	        numberAxis.setRange(-10000000,10000000);   

	        frame.pack();
	        frame.setVisible(true);
	    }  
}

           
       