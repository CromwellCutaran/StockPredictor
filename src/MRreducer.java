import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.io.Text;
import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.text.DecimalFormat;

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
		   double open = 0, close = 0, volume = 0;
		   
		   String compositeString;
		   String[] compositeStringArray;
		   double total = 0, first = 0, last = 0, count = 0;
		   
		   for(Text value: values)
	       {
	    		compositeString = value.toString();
	    		compositeStringArray = compositeString.split(IFS);
	    		date = compositeStringArray[0];
	    		open = Double.parseDouble(compositeStringArray[1]);
	    		close = Double.parseDouble(compositeStringArray[4]);
	    		volume = Double.parseDouble(compositeStringArray[5]);
	    		
	    		total += (close-open)/open; // add daily change to total
	    		count++; // increase counter
	    		
	    		//set first or last close values that year, 01-02 & 12-31
	    		//for later calculating growth rate that year
	    		if(date.contains("01-02")){
	    			first = close;
	    		}
	    		else if(date.contains("12-31")){
	    			last = close;
	    		}
	    		
	    		/*
	    		 * calculate the OBV -- momentum indicator that uses volume
	    		 * flow to predict changes in stock price
	    		 */
	       }
		   
		   DecimalFormat df = new DecimalFormat("##.##%");

		   //calculate average daily growth rate (present-past)/past
		   double dailyAvg = total/count;
		   String dailyAvgFormatted = df.format(dailyAvg);
		   
		   //calculate growth rate for the year
		   double growthRate = (last-first)/first;
		   String growthRateFormatted = df.format(growthRate);
		   
		   context.write(new Text("YEAR: "), key);
		   Text keyText1 = new Text("average daily growth rate: ");
		   context.write(keyText1, new Text(dailyAvgFormatted));
		   Text keyText2 = new Text("growth rate for year " + key + ": ");
		   context.write(keyText2, new Text(growthRateFormatted));
		   
	   }

}
