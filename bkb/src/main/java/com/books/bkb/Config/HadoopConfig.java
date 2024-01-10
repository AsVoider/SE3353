package com.books.bkb.Config;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HadoopConfig {

    @Bean
    public org.apache.hadoop.conf.Configuration config() {
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("fs.defaultFS", "hdfs://localhost:9700");
        configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        configuration.setBoolean("dfs.client.block.write.replace-datanode-on-failure.enabled", true);
        return configuration;
    }

    @Bean
    public FileSystem getFileSystem() {
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(config());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileSystem;
    }
}
