package com.xli.study.concurrent.juc;

import java.util.concurrent.locks.LockSupport;

public class LocksuportDemo {

    static Thread t1 =null;
    static Thread t2 =null;

    public static void main(String[] args) {
        char[] c1 = "123456".toCharArray();
        char[] c2 = "ABCDEF".toCharArray();

        t1 = new Thread("t1"){
            @Override
            public void run() {
                for(char c:c1){
                    System.out.print(c);
                    LockSupport.unpark(t2);
                    LockSupport.park(); // 当前线程阻塞
                }
            }
        };
        t2 = new Thread("t2"){
            @Override
            public void run() {
                for(char c:c2){
                    LockSupport.park(); // 当前线程阻塞
                    System.out.print(c);
                    LockSupport.unpark(t1);

                }
            }
        };


        t1.start();
        t2.start();



    }
}
