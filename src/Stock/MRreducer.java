package Stock;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class MRreducer  extends Reducer <Text,Text,Text,Text> {

	   public static String IFS=",";
	   public static String OFS=",";
	   public static String divider = "*****";
	   public int i = 1;
	   private ArrayList<ArrayList<Object>> allData = new ArrayList<ArrayList<Object>>();
	   
	   public void reduce(Text key, Iterable<Text> values, Context context) 
			   throws IOException, InterruptedException {		   		   			   
		   
	        /** values consist of:
	         * date
	         * open
	         * high
	         * low
	         * close
	         * volume
	         * name
	        */		    		 		  
		   		  
		   //initialize variables 
		   String date = null;
		   double open = 0, close = 0, volume = 0;
		   
		   String compositeString;
		   String[] compositeStringArray;
		   double total = 0, first = 0, last = 0, count = 0;
		   double previousClose = 0, previousOBV = 0, currentOBV = 0;
		   
		   // marker for calculating OBVs
		   boolean start = true;		   

		   for(Text value: values)
	       {
	    		compositeString = value.toString();
	    		compositeStringArray = compositeString.split(IFS);

	    		try {
		    		date = compositeStringArray[0];
		    		open = Double.parseDouble(compositeStringArray[1]);
		    		close = Double.parseDouble(compositeStringArray[4]);
		    		volume = Double.parseDouble(compositeStringArray[5]);
	    		} catch (Exception e) {
	    			continue;
	    		}
	    		
	    		// increment total with daily change
	    		total += (close-open)/open;
	    		// increment day counter
	    		count++; 

	    		// parse dates to determine first and last closing price of every year
	    		if (date.contains("2012-08-")) {
	    			first = close;
	    		} else if (date.contains("01-")) {
	    			first = close;
	    		} else if (date.contains("2017-08-")) {
	    			last = close;
	    		} else if (date.contains("12-")) {
	    			last = close;
	    		}
	    		
	    		// calculate the OBV -- momentum indicator that uses
	    		// volume flow to predict changes in stock price	    			    		
	    		if (start) {
	    			start = false;
	    			previousClose = close;
	    		} else {
	    			if (close > previousClose) {
	    				currentOBV = previousOBV + volume;
	    			} else if (close < previousClose) {
	    				currentOBV = previousOBV - volume;	    			
	    			} else if (close == previousClose) {
	    				currentOBV = previousOBV;
	    			}
	    			
	    			
	    			previousClose = close;
	    			previousOBV = currentOBV;
	    			
	    			context.write(new Text(currentOBV + " " + close), new Text(date));    	    		
	    		}
	       }
		   
		   //calculate average daily growth rate (present-past)/past
		   double dailyAvg = total/count;
		   
		   //calculate growth rate over the entire year
		   double growthYear = (last-first)/first;
	   		   
		   // data = [key, dailyAvgFormatted, growthRateFormatted, ""]
		   ArrayList<Object> data = new ArrayList<>();
		   data.add(dailyAvg);
		   data.add(growthYear);
		   data.add(new Text(""));
		   
		   allData.add(data);

		   // if at last key, printAverageData()
		   if (key.toString().equals("2017")) {
			   printAverageData(allData, context);
		   }
	   }
	   
	   public void printAverageData (ArrayList<ArrayList<Object>> allData, Context context) throws IOException, InterruptedException {
		   Text keyTextHeader = new Text("YEAR: ");
		   Text keyTextGrowthDaily = new Text("Daily Growth Rate: ");
		   Text keyTextGrowthYear = new Text("Growth Rate for Year: ");
		   Text keyTextFooter = new Text("");
		   
		   DecimalFormat df = new DecimalFormat("##.###%");
		   int startYear = 2012;		   
		   
		   // insert divider and begin writing average data
		   Text keyTextDivider = new Text("*****");
		   context.write(keyTextDivider, new Text(""));		   
		   Text keyTextCompany = new Text("Growth rates for " + context.getJobName() +
				   						  "\n----------------------");
		   context.write(keyTextCompany, new Text(""));
		   		   		   
		   for (ArrayList<Object> data : allData) {
			   double dailyAvg = (double) data.get(0);
			   double growthRate = (double) data.get(1);
			   Text valueTextGrowthDaily = new Text(Double.toString(dailyAvg) + "  (" + df.format(dailyAvg) + ")");
			   Text valueTextGrowthYear = new Text(Double.toString(growthRate) + "  (" + df.format(growthRate) + ")");
			   Text footer = (Text) data.get(2);			   
			   
			   context.write(keyTextHeader, new Text(startYear + ""));			   			   
			   context.write(keyTextGrowthDaily, valueTextGrowthDaily);			   
			   context.write(keyTextGrowthYear, valueTextGrowthYear);			   
			   context.write(keyTextFooter, footer);
			   
			   startYear++;
		   }		   
	   }
}