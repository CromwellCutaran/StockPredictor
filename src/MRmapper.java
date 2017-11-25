
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

//import com.sun.jersey.core.spi.scanning.Scanner;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import java.io.IOException;
import java.util.Scanner;

public class MRmapper  extends Mapper <LongWritable,Text,Text,Text>  {
	
    static String IFS=",";
    static String OFS=",";
    static int NF=11;
    static int badRecordCounter =0;
    
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        
	
	/*Open: The price at the beginning of the trading day
	High: The highest price the stock reached during the day
	Low: The lowest price reached during the day
	Close: The final price*/
    	Scanner input = new Scanner(value.toString().trim());  //instantiate line
	String line = input.nextLine().trim();         //Read next line  ... trim: removes trailing spaces
 	String lineArray[] = line.split(",");
 	
 	String date = lineArray[0].trim();
 	String open = lineArray[1].trim();
 	String high = lineArray[2].trim();
 	String low = lineArray[3].trim();
 	String close = lineArray[4].trim();
 	String volume = lineArray[5].trim();
 	String name = lineArray[6].trim();
 	
 	String valueForKey = date + " " + open + " " + high + " " + low  + " " + close + " " + volume;
 	
 	context.write(new Text(name), new Text(valueForKey));
 	
    	
    	
    }

}
