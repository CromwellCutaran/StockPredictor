package Stock;

import java.util.Scanner;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

public class MRdriver extends Configured implements Tool {
    @SuppressWarnings("deprecation")
   public int run(String[] args) throws Exception {
    	//get user input
    	Scanner reader = new Scanner(System.in);
    	System.out.println("Enter a company: ");
    	String n = reader.next();
    	reader.close();
    	
        // TODO 1: configure  MR job
		Job job = new Job(getConf(), n); // "stocks" = string job name
		job.setJarByClass(MRdriver.class);
		job.setMapperClass(MRmapper.class);
		job.setReducerClass(MRreducer.class);
		job.setInputFormatClass(TextInputFormat.class); //files from plain txt files broken into lines
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapOutputValueClass(Text.class);
        // TODO 2: setup input and output paths for MR job
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
        // TODO 3: run  MR job syncronously with verbose output set to true
		return job.waitForCompletion(true) ? 0 : 1;
   }

   public static void main(String[] args) throws Exception { 
       if(args.length != 2) {
           System.err.println("usage: MRdriver <input-path> <output-path>");
           System.exit(1);
       }
       Configuration conf = new Configuration();
       System.exit(ToolRunner.run(conf, new MRdriver(), args));
   } 
}