package com.books.bkb.utils.Hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

public class WordCount {
    public static class TokenMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final Text word = new Text();
        private final static IntWritable one = new IntWritable(1);
        String[] strs = {"java", "python", "c++", "rust"};
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                String s=word.toString();
//                String regEx = "[` ,./12345678'™]";
//                String s1=s.replaceAll(regEx," ");
//                Text word2 = new Text(s1);
                if (Arrays.asList(strs).contains(s.toLowerCase()))
                    context.write(word, one);
            }
        }
    }

    public static class IntReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private final IntWritable res = new IntWritable();
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (var val : values) {
                sum += val.get();
            }
            res.set(sum);
            context.write(key, res);
        }
    }
}
