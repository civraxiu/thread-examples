package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Statistics {

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        
        String homePath = System.getProperty("user.home");
        File filesDir = new File(homePath, "somma");

        long sum = 0;
        long t0 = System.currentTimeMillis();
        
        for (File file : filesDir.listFiles()) {
            Somma somma = new Somma(file);
            Future<Long> futureFileSum = executor.submit(somma);
            sum += futureFileSum.get();
        }
        
        long time = System.currentTimeMillis() - t0;

        System.out.println("Somma: " + sum);
        System.out.println("Tempo impiegato: " + time + " msec");
    }

    private static class Somma implements Callable<Long> {

        private File file;

        private Somma(File file) {
            this.file = file;
        }

        @Override
        public Long call() {
            
            try (Scanner sc = new Scanner(file)) {
                     
                long sum = 0;

                while(sc.hasNext()) {
                    sum += sc.nextLong();
                }
                return sum;
                
            } catch (FileNotFoundException e) {
                System.err.println("File non trovato: " + file.getAbsolutePath());
                throw new RuntimeException(e);
            }
            
        }
    }
}
