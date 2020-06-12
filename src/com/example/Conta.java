package com.example;

import java.util.Random;

class Conta {

    public static void main(String args[]) {
        P T1 = new P('A');
        P T2 = new P('B');
        T1.start();
        T2.start();
    }
}

class P extends Thread {

    char ch;
    int i = 1, j = 20;// inizializzazione delle variabili

    public P(char c) {
        ch = c;
    }// costruttore

    public void run() {
        if (ch == 'A')
            while (i<=10) {
                System.out.println(i);
                i++;
                try {
                    Thread.sleep(new Random().nextInt(800)+200);
                } catch (InterruptedException e) {
                }
            }
        else
            while (j > 10) {
                System.out.println(" " + j);
                j--;
                try {
                    Thread.sleep(new Random().nextInt(300)+200);
                } catch (InterruptedException e) {
                }
            }
    }
}