package Stock;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.io.Text;

import java.awt.Color;
import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.text.DecimalFormat;

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

public class MRreducer  extends Reducer <Text,Text,Text,Text> {

	   public static String IFS=",";
	   public static String OFS=",";
	   public void reduce(Text key, Iterable<Text> values, Context context) 
			   throws IOException, InterruptedException {		   		   			   
		   
	        /** values consist of:
	         * date
	         * open
	         * high
	         * low
	         * close
	         * volume
	        */
		   
		   //initialize variables 
		   String date = null;
		   String name;
		   double open = 0, close = 0, volume = 0;
		   
		   String compositeString;
		   String[] compositeStringArray;
		   double total = 0, first = 0, last = 0, count = 0;
		   
//		   XYDataset inputData; 
//		   
//			XYSeriesCollection dataset = new XYSeriesCollection();  // create dataset 
//			XYSeries series = new XYSeries("Prices");    //type
		   
		   for(Text value: values)
	       {
			   
			   if (key.toString().equals("2017")) {
			   		System.out.println("Start of for loop: " + value);
			   	}
			   
	    		compositeString = value.toString();
	    		compositeStringArray = compositeString.split(IFS);

	    		
//	    		2017-07-31,,201.66,,201.17,1833625.0,MMM
	    		try {
		    		date = compositeStringArray[0];
		    		open = Double.parseDouble(compositeStringArray[1]);
		    		close = Double.parseDouble(compositeStringArray[4]);
		    		volume = Double.parseDouble(compositeStringArray[5]);
		    		name = compositeStringArray[6];
	    		} catch (Exception e) {
	    			continue;
	    		}
	    		if (key.toString().equals("2017")) {
			   		System.out.println("After try-catch: " + value);
			   	}
	    		
//	    		context.write(key, new Text(date + OFS + open + OFS + close + OFS + volume));
//	    		
	    		total += (close-open)/open; // add daily change to total
	    		count++; // increase counter
	    		
	    		//set first or last close values that year, 01-02 & 12-31
	    		//for later calculating growth rate that year
	    		
	    		if (date.contains("2012-08-")) {
	    			first = close;
	    		} else if (date.contains("01-")) {
	    			first = close;
	    		} else if (date.contains("2017-08-")) {
	    			last = close;
	    		} else if (date.contains("12-")) {
	    			last = close;
	    		}
	    		
	    		if (key.toString().equals("2017")) {
			   		System.out.println("After elif block: " + value);
			   	}
	    		
	    		/*
	    		 * calculate the OBV -- momentum indicator that uses volume
	    		 * flow to predict changes in stock price
	    		 */
	    			double dateSet = Double.parseDouble(date.replace("-", "")); 
//				series.add(close, dateSet);
	    			
//	    		context.write(new Text("total, count, first, last, name"), new Text(Double.toString(total) + ", " + count + ", " + first + ", " + last + ", " + name));
	       }
//		   dataset.addSeries(series);
//		  inputData = dataset;
//		   JFreeLineChart demo = new JFreeLineChart(inputData);
//			demo.pack();
//			RefineryUtilities.centerFrameOnScreen(demo);
//			demo.setVisible(true);
			
//		   DecimalFormat df = new DecimalFormat("##.##%");
		  
		   
		   //calculate average daily growth rate (present-past)/past
		   double dailyAvg = total/count;
		   String dailyAvgFormatted = Double.toString(dailyAvg);
		   
		   //calculate growth rate for the year
		   double growthRate = (last-first)/first;
		   String growthRateFormatted = Double.toString(growthRate);
		   
		   if (key.toString().equals("2017")) {
		   		for (Text value:values) {
		   			System.out.println(value.toString());
		   		}		   		
		   	}
		   
		   
		   Text keyTextHeader = new Text("YEAR: ");		   
		   context.write(keyTextHeader, key);
		   
		   Text keyTextGrowthDaily = new Text("Daily Growth Rate: ");
		   context.write(keyTextGrowthDaily, new Text(dailyAvgFormatted));
		   
		   Text keyTextGrowthYear = new Text("Growth Rate for Year: ");
		   context.write(keyTextGrowthYear, new Text(growthRateFormatted));		
		   
		   Text keyTextFooter = new Text("");
		   context.write(keyTextFooter, new Text(""));
	   }
	   
//	   @Override
//	   protected void cleanup(Context context) {
//		   Text keyTextTest = new Text("KEYS: ");
//		   context.write(keyTextTest, context.getCurrentKey());
//	   }
}
