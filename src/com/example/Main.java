package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static ExecutorService ec = Executors.newFixedThreadPool(2);

    private static Runnable repeat(String str) {
        return () -> {
            var id = Thread.currentThread().getId();
            
            synchronized (System.out) {                
                System.out.print("[Thread-" + id + "] ");
                str.repeat(50).chars().forEach(ch -> {
                    System.out.print((char) ch);
                });
                System.out.println();
            }
        };
    }

    public static void main(String[] args) {

        var th1 = repeat("A");
        var th2 = repeat("B");

        ec.submit(th1);
        ec.submit(th2);

        System.out.println("Entrambi i thread sono partiti!");
        ec.shutdown();
    }

}
