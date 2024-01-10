package com.books.bkb.Service.imply;

import com.books.bkb.Service.inter.HadoopServe;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;
//import scala.Tuple2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HadoopImp implements HadoopServe {

    FileSystem fileSystem;
    @Resource
    SparkSession session;
    @Resource
    JavaSparkContext context;

    @Autowired
    public HadoopImp(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
//        this.session = session;
//        this.context = context;
    }

    @Override
    public String readFile(String path) {
        if (StringUtils.isEmpty(path)){
            return null;
        }

        if (!existFile(path)) {
            return null;
        }

        Path src = new Path(path);

        FSDataInputStream inputStream = null;
        StringBuilder sb = new StringBuilder();
        try {
            inputStream = fileSystem.open(src);
            String lineText = "";
            while ((lineText = inputStream.readLine()) != null) {
                sb.append(lineText);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return sb.toString();
    }

    @Override
    public boolean existFile(String path) {
        if (StringUtils.isEmpty(path)){
            return false;
        }
        Path src = new Path(path);
        try {
            return fileSystem.exists(src);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean createDir(String path) {
        boolean target = false;
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (existFile(path)) {
            return true;
        }
        Path src = new Path(path);
        try {
            target = fileSystem.mkdirs(src);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return target;
    }

    @Override
    public void writeFile(String path, String content) {
        FSDataOutputStream fsDataOutputStream = null;
        try {
            Path hdfsPath = new Path(path);
            if (!fileSystem.exists(hdfsPath)) {
                fsDataOutputStream = fileSystem.create(hdfsPath,false);
                fsDataOutputStream.writeBytes(content);
                fsDataOutputStream.close();
                System.out.println("create here");
            }else{
                fsDataOutputStream = fileSystem.append(hdfsPath);
                fsDataOutputStream.writeBytes("\n"+content);
                fsDataOutputStream.close();
                System.out.println("appendhere");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void write2local(String path, String content) throws IOException {
        File file = new File(path);
        if (!file.exists())
            file.createNewFile();
        FileWriter writer = new FileWriter(file, true);
        writer.write(content);
        writer.close();
    }

    @Override
    public Map<String, Integer> sparkTask(String[] filenames, String[] words) {
        JavaRDD<String> fileRdd = context.textFile("/mydata/hadoop/input2hadoop/*.txt");
        JavaRDD<String> wordRdd = fileRdd.flatMap(
                line -> Arrays.asList(line.split(" ")).iterator()
        );
        JavaPairRDD<String, Integer> wordOne = wordRdd
                .filter(word -> Arrays.asList(words).contains(word.toLowerCase()))
                .mapToPair(
                word -> new Tuple2<>(word.toUpperCase(), 1)
        );
        JavaPairRDD<String, Integer> wordCount = wordOne.reduceByKey(Integer::sum);
        List<Tuple2<String, Integer>> res = wordCount.collect();
        Map<String, Integer> returnMap = new HashMap<>();
        for (var item : res) {
            //System.out.println(item._1 +" " + item._2);
            returnMap.put(item._1, item._2);
        }
        return returnMap;
    }
}
