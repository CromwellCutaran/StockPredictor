package Stock;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.io.Text;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.text.DecimalFormat;


public class MRreducer  extends Reducer <Text,Text,Text,Text> {

	   public static String IFS=",";
	   public static String OFS=",";
	   public int i = 1;
	   
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
		   //file writer 
		 
		   FileWriter pw = new FileWriter("data" + i +".txt");  
		   i++;
		   //initialize variables 
		   String date = null;
		   String name;
		   double open = 0, close = 0, volume = 0;
		   
		   String compositeString;
		   String[] compositeStringArray;
		   double total = 0, first = 0, last = 0, count = 0;
		   double previousClose = 0, previousOBV = 0, currentOBV = 0;
		   
		   boolean start = true;

		   for(Text value: values)
	       {
			   
			   if (key.toString().equals("2017")) {
			   		System.out.println("Start of for loop: " + value);
			   	}
			   
	    		compositeString = value.toString();
	    		compositeStringArray = compositeString.split(IFS);

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
	    		
	    		/*
	    		 * calculate the OBV -- momentum indicator that uses volume
	    		 * flow to predict changes in stock price
	    		 */	    		
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
	    			
	    			// test OBV calculation
	    			System.out.println("volume: " + volume + "\n" +
 						   "previousClose: " + previousClose + "\n" +
 						   "close: " + close + "\n" +
 						   "previousOBV: " + previousOBV + "\n" +
 						   "currentOBV: " + currentOBV + "\n");
	    			
	    			previousClose = close;
	    			previousOBV = currentOBV;
	    			
	    			// write currentOBV to text file
	    			
	    			//String dateSet = date.replace("-", "").substring(4);
	    			   
	    	        context.write(new Text(currentOBV + " ") ,new Text(date));    

	    			
	    		}	
	    		
	    		
	       }

//		   //calculate average daily growth rate (present-past)/past
//		   double dailyAvg = total/count;
//		   String dailyAvgFormatted = Double.toString(dailyAvg);
//		   
//		   //calculate growth rate for the year
//		   double growthRate = (last-first)/first;
//		   String growthRateFormatted = Double.toString(growthRate);
//		   
//		   Text keyTextHeader = new Text("YEAR: ");		   
//		   context.write(keyTextHeader, key);
//		   
//		   Text keyTextGrowthDaily = new Text("Daily Growth Rate: ");
//		   context.write(keyTextGrowthDaily, new Text(dailyAvgFormatted));
//		   
//		   Text keyTextGrowthYear = new Text("Growth Rate for Year: ");
//		   context.write(keyTextGrowthYear, new Text(growthRateFormatted));		
//		   
//		   Text keyTextFooter = new Text("");
//		   context.write(keyTextFooter, new Text(""));
	   }
}