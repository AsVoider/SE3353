package com.books.bkb.Controller;

import com.books.bkb.Service.inter.BookServe;
import com.books.bkb.Service.inter.HadoopServe;
import com.books.bkb.utils.Hadoop.WordCount;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;

@RestController
public class HadoopController {
    static String inputPath = "/testinput/input2hadoop";
    static String outputPath = "/testoutput";
    static String localpath = "/mydata/hadoop/input2hadoop";
    BookServe bookServe;
    Configuration configuration;
    HadoopServe hadoopServe;
    public HadoopController(BookServe bookServe, Configuration configuration, HadoopServe hadoopServe) {
        this.bookServe = bookServe;
        this.configuration = configuration;
        this.hadoopServe = hadoopServe;
    }

    @GetMapping("/public/writefile")
    public void write2file() {
        var books = bookServe.getBooks();
        for (var book : books) {
            if (Objects.equals(book.getTypes(), "教育")) {
                hadoopServe.writeFile(inputPath + "/edu.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "高中教辅")) {
                hadoopServe.writeFile(inputPath + "/high.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "科普")) {
                hadoopServe.writeFile(inputPath + "/sci.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "大学教材")) {
                hadoopServe.writeFile(inputPath + "/uni.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "名著")) {
                hadoopServe.writeFile(inputPath + "/fam.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "杂志")) {
                hadoopServe.writeFile(inputPath + "/maz.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "文学")) {
                hadoopServe.writeFile(inputPath + "/lib.txt", book.getDescription());
            }
        }
    }

    @GetMapping("/public/write2local")
    public void write2local() throws IOException {
        var books = bookServe.getBooks();
        for (var book : books) {
            if (Objects.equals(book.getTypes(), "教育")) {
                hadoopServe.write2local(localpath + "/edu.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "高中教辅")) {
                hadoopServe.write2local(localpath + "/high.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "科普")) {
                hadoopServe.write2local(localpath + "/sci.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "大学教材")) {
                hadoopServe.write2local(localpath + "/uni.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "名著")) {
                hadoopServe.write2local(localpath + "/fam.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "杂志")) {
                hadoopServe.write2local(localpath + "/maz.txt", book.getDescription());
            } else if (Objects.equals(book.getTypes(), "文学")) {
                hadoopServe.write2local(localpath + "/lib.txt", book.getDescription());
            }
        }
    }

    @GetMapping("/public/wordcount")
    public void WordCount() throws IOException, InterruptedException, ClassNotFoundException {
        Job job = Job.getInstance(configuration);
        job.setMapperClass(WordCount.TokenMapper.class);
        job.setCombinerClass(WordCount.IntReducer.class);
        job.setReducerClass(WordCount.IntReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.setInputPaths(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        System.out.println("job reducer num : " + job.getNumReduceTasks());
        job.waitForCompletion(true);
    }

    @GetMapping("/public/sparkcount")
    public ResponseEntity<?> SparkCount() {
        String[] words = {"java", "c++", "rust", "python"};
        var ret = hadoopServe.sparkTask(new String[] {}, words);
        return ResponseEntity.ok(ret);
    }
}
