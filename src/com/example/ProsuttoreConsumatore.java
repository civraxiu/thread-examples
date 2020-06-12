package com.example;

import java.util.Random;

public class ProsuttoreConsumatore {

    public static void main(String[] args) throws InterruptedException {

        Buffer buffer = new Buffer();

        new Thread(() -> {
            try {
                buffer.test();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                buffer.test();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        Thread.sleep(2000);
        buffer.doNotify();
    }

    private static class Buffer {

        private int value;
        private boolean empty = true;

        synchronized public void doWait() throws InterruptedException {
            System.out.println("Thread-" + Thread.currentThread().getId() + ": waiting");
            wait();

            System.out.println("Thread-" + Thread.currentThread().getId() + ": resumed");
        }

        synchronized public void doNotify() throws InterruptedException {
            notifyAll();
        }

        synchronized public void test() throws InterruptedException {
            System.out.println("Thread-" + Thread.currentThread().getId() + ": test");
            Thread.sleep(2000);
        }
        
        synchronized public void setValue(int value) throws InterruptedException {
            if (!this.empty)
                wait();

            this.value = value;
            this.empty = false;
            notifyAll();
        }

        synchronized public int readValue() throws InterruptedException {
            if (this.empty)
                wait();
            return this.value;
        }

        synchronized public int getValue() throws InterruptedException {
            if (this.empty)
                wait();

            this.empty = true;
            notifyAll();
            return this.value;
        }
    }

    private static class Produttore implements Runnable {

        private static final Random random = new Random();
        private Buffer buffer;

        public Produttore(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {

            try {
                for (int i = 0; i < 10; i++) {
                    int value = random.nextInt(10) + 1;
                    buffer.setValue(value);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private static class Consumatore implements Runnable {

        private Buffer buffer;

        public Consumatore(Buffer buffer, int min, int max) {
            this.buffer = buffer;
        }

        @Override
        public void run() {

            try {
                int value = buffer.getValue();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
