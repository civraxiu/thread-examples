package com.example.solutions;

public class ProduttoreConsumatore {

    public static final String termination = "DONE";
    
    public static void main(String[] args) {

        Drop drop = new Drop();
        
        Produttore p = new Produttore(termination, drop);
        Consumatore c = new Consumatore(termination, drop);
        
        new Thread(p).start();
        new Thread(c).start();
    }

    // Produttore.java
    private static class Produttore implements Runnable {

        private final Drop drop;
        private final String termination;
        private final String[] messages = new String[] {
            "Ciao,",
            "sono il produttore",
            "arrivederci"
        };
        
        public Produttore(String termination, Drop drop) {
            this.termination = termination;
            this.drop = drop;
        }
        
        @Override
        public void run() {
            for (String message : messages) {
                
                drop.put(message);
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            drop.put(termination);
            System.out.println("Produttore terminato");
        }
    }

    // Consumatore.java
    private static class Consumatore implements Runnable {

        private final Drop drop;
        private final String termination;

        public Consumatore(String termination, Drop drop) {
            this.termination = termination;
            this.drop = drop;
        }
        
        @Override
        public void run() {
            String message;
            do {
                message = drop.take();
                System.out.println(message);
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            } while(!message.equals(termination));
            
            System.out.println("Consumatore terminato");
        }
    }

    // Drop.java
    private static class Drop {

        // Message sent from producer to consumer.
        private String message;

        // True if consumer should wait for producer to send message,
        // false if producer should wait for consumer to retrieve message.
        private boolean empty = true;

        public synchronized String take() {
            // Wait until message is available.
            while (empty) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            // Toggle status.
            empty = true;
            // Notify producer that status has changed.
            notifyAll();
            return message;
        }

        public synchronized void put(String message) {
            // Wait until message has been retrieved.
            while (!empty) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            // Toggle status.
            empty = false;
            // Store message.
            this.message = message;
            // Notify consumer that status has changed.
            notifyAll();
        }

    }
}
