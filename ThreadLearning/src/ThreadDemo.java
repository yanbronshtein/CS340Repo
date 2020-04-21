//package com.tutorialspoint;

import java.lang.*;

public class ThreadDemo implements Runnable {

    Thread t;

    ThreadDemo() {

        t = new Thread(this);
        System.out.println("Executing " + t.getName());

        // this will call run() fucntion
        t.start();

        // interrupt the threads
        if (!t.interrupted()) {
            t.interrupt();
        }

        // block until other threads finish
        try {
            t.join();
        } catch(InterruptedException e) {}
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.print(t.getName() + " interrupted:");
            System.out.println(e.toString());
        }
    }

    public static void main(String args[]) {
        new ThreadDemo();
        new ThreadDemo();
    }
}