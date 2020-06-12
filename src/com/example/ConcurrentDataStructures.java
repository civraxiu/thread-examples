package com.example;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentDataStructures {

    private final static ExecutorService ec = Executors.newFixedThreadPool(30);
    private final static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {

        var map = new HashMap<String, Integer>();

        var N = 1000;

        for (int i = 0; i < N; i++) {

            var key = "key-" + random.nextInt(1000);

            ec.execute(() -> {
                try {
                    Thread.currentThread().sleep(random.nextInt(500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                var value = random.nextInt(1000);
                map.put(key, value);
            });

            ec.execute(() -> {
                System.out.println(map.get(key));
            });
        }

        System.out.println("Shutdown...");
        ec.shutdown();
        ec.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println(map);
    }

}
