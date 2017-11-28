package Stock;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;

import java.io.IOException;
import java.util.StringTokenizer;

public class MRmapper  extends Mapper <LongWritable,Text,Text,Text> {
    static String IFS=",";
    static String OFS=",";
    
    public void map(LongWritable key, Text value, Context context) 
                    throws IOException, InterruptedException {
        
        /** stocks.csv
        date
        open
        high
        low
        close
        volume
        name
        */
    	
    	/*Open: The price at the beginning of the trading day
    	High: The highest price the stock reached during the day
    	Low: The lowest price reached during the day
    	Close: The final price*/
    	
    	//get job name
    	String company = context.getJobName();
        
        // TODO 1: remove schema line
    	//the key is the bite offset into the file, so schema line key = 0
    	if(key.get() == 0){
    		return;
    	}
    	
    	// TODO 2: convert value to string
    	String lineString = value.toString();
    	String[] lineArray = lineString.split(IFS);

        // TODO 4: pull out fields of interest:
        String date = lineArray[0];
        String[] dateArray = date.split("-");
        String year = dateArray[0];
        String open = lineArray[1];
        String high = lineArray[2];
        String low = lineArray[3];
        String close = lineArray[4];
        String volume = lineArray[5];
        String name = lineArray[6];
        
        //check if user company query matches
        if (!name.equals(company)) {
        		return;
        }
        
		String valueForKey = date + OFS + open + OFS + high + OFS + low  + OFS + close + OFS + volume + OFS + name;
		
		System.out.println(year);
		
		Text yearText = new Text(year);
		context.write(yearText, new Text(valueForKey));
    }
}
