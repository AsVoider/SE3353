package com.books.bkb.Service.inter;

import java.io.IOException;
import java.util.Map;

public interface HadoopServe {
    String readFile(String path);
    void writeFile(String path, String content);
    boolean createDir(String path);
    boolean existFile(String path);
    void write2local(String path, String content) throws IOException;
    Map<String, Integer> sparkTask(String[] filenames, String[] words);
}
