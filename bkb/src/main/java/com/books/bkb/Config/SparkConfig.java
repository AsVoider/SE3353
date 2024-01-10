package com.books.bkb.Config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {
    private static String appName = "WordCount";
    private static String MasterUri = "local[*]";

    @Bean
    public SparkConf sparkConf() {
        return new SparkConf().setAppName(appName)
                .setMaster(MasterUri)
                .set("spark.ui.enabled","false");
    }

    @Bean
    @ConditionalOnMissingBean(JavaSparkContext.class)
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public SparkSession session() {
        return SparkSession.builder()
                .sparkContext(javaSparkContext().sc())
                .getOrCreate();
    }


}
