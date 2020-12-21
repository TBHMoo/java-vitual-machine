package com.xli.study.concurrent.juc;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class TransferQueueDemo {

    public static void main(String[] args) {

        TransferQueue<Character> transferQueue= new LinkedTransferQueue<>();

        char[] aI = "123456".toCharArray();
        char[] aC = "ABCDEF".toCharArray();

        new Thread(()->{

            try {
                for (char c:aI){
                    System.out.print(transferQueue.take());
                    transferQueue.transfer(c);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t1").start();


        new Thread(()->{

            try {
                for (char c:aC){
                    transferQueue.transfer(c);
                    System.out.print(transferQueue.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t2").start();
    }
}
