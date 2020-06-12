package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LogFile {

    private static final Random random = new Random();

    private Writer out;

    public LogFile(File f) throws IOException {
        FileWriter fw = new FileWriter(f, true);
        this.out = new BufferedWriter(fw);
    }

    private int randomWait() {
        return random.nextInt(100);
    }
    
    public void writeEntry(String message) throws Exception {
        synchronized (out) {
            var id = Thread.currentThread().getId();
            Date d = new Date();
            out.write(d.toString());
            out.flush();
            Thread.currentThread().sleep(randomWait());
            out.write(" [Thread-" + id + "]");
            out.flush();
            Thread.currentThread().sleep(randomWait());
            out.write(" [" + out.toString() + "]");
            out.flush();
            Thread.currentThread().sleep(randomWait());
            out.write('\t');
            out.flush();
            Thread.currentThread().sleep(randomWait());
            out.write(message);
            out.flush();
            Thread.currentThread().sleep(randomWait());
            out.write("\r\n");
            out.flush();
            Thread.currentThread().sleep(randomWait());
        }
    }

    public void close() throws IOException {
        out.flush();
        out.close();
    }

    public static void main(String[] args) throws Exception {
        ExecutorService ec = Executors.newFixedThreadPool(30);
        var tmpFile = new File("/home/nicola/tmp");
        var log1 = new LogFile(tmpFile);
//        var log2 =  log1;
        var log2 = new LogFile(tmpFile);

        var N = 10;
        for (int i=0; i<N; i++) {
            
            ec.submit(() -> {
                try {
                    log1.writeEntry("hello");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            ec.submit(() -> {
                try {
                    log2.writeEntry("ciao");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        
        ec.shutdown();
        ec.awaitTermination(20, TimeUnit.SECONDS);
        
        log1.close();
        log2.close();

        FileReader fr = new FileReader(tmpFile);
        BufferedReader r = new BufferedReader(fr);
        r.lines().forEach(System.out::println);
        r.close();
    }
}

