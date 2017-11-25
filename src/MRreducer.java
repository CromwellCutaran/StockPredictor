import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.io.Text;
import java.io.IOException;

public class MRreducer  extends Reducer <Text,Text,Text,Text> {

	   public static String IFS=",";
	   public static String OFS=",";
	   public void reduce(Text key, Iterable<Text> values, Context context) 
			   throws IOException, InterruptedException {
		   
		   //initialize variables 
		   String date = null, open = null, high = null, low = null, close = null, volume = null, name = null;
		   int projection = 0;
		   
		   for(Text value: values)
	       {
			   String compositeString =  value.toString().trim(); //
			   String[] compositeStringArray;
	       }
		   
	   }

}
