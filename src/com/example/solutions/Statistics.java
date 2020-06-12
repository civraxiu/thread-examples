package com.example.solutions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Statistics {

    private static ExecutorService executor = Executors.newFixedThreadPool(200);
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        
        long t0 = System.currentTimeMillis();
        
        String homePath = System.getProperty("user.home");
        File filesDir = new File(homePath, "somma");

        int sum = 0;
        
        List<Future<Integer>> futureValues = new ArrayList<>();
        
        for (File file : filesDir.listFiles()) {
            
            if (file.isDirectory())
                continue;
            
            Somma somma = new Somma(file.getAbsolutePath());
            Future<Integer> futureSum = executor.submit(somma);
            futureValues.add(futureSum);
        }
        
        for (Future<Integer> futureFileSum : futureValues) {
            sum += futureFileSum.get();
        }
        
        long elapsed = System.currentTimeMillis() - t0;
        
        System.out.println("Somma: " + sum);
        System.out.println("Tempo di esecuzione: " + elapsed);
        executor.shutdown();
    }

}

class Somma implements Callable<Integer>{
    
    private String file;
    
    public Somma(String file) {
        this.file = file;
    }
    
    @Override
    public Integer call() {
        
        try(
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr)) {
            
            Thread.sleep(100);
            
            int sum = 0;
            
            while(br.ready()) {
                              
                String line = br.readLine();
                String[] values = line.split("\\s+");
                
                for (String value: values) {
                    sum += Integer.parseInt(value);
                }
            }
            
            return sum;
            
        } catch (FileNotFoundException e) {
            System.err.println("File non trovato: " + file);
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Impossibile leggere il file: " + file);
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
