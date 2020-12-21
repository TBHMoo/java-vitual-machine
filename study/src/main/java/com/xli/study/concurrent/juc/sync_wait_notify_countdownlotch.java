package com.xli.study.concurrent.juc;

import java.util.concurrent.CountDownLatch;

public class sync_wait_notify_countdownlotch {

    final static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) {


        final Object o = new Object();

        char[] c1 = "123456".toCharArray();
        char[] c2 = "ABCDEF".toCharArray();

        new Thread(()->{
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (o){
                for(char c:c1){
                    System.out.print(c);
                    try {
                        o.notify();
                        o.wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        },"t1").start();

        new Thread(()->{
            synchronized (o){
                for(char c:c2){
                    System.out.print(c);
                    if(latch.getCount()>0){
                        latch.countDown();
                    }
                    try {
                        o.notify();
                        o.wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        },"t2").start();
    }

}
