package com.xli.study.juc.sss;

public class sync_wait_notify {

    public static void main(String[] args) {

        final Object o = new Object();

        char[] c1 = "123456".toCharArray();
        char[] c2 = "ABCDEF".toCharArray();

        new Thread(()->{
            synchronized (o){
                for(char c:c1){
                    System.out.print(c);
                    try {
                        o.notify(); // 随机唤醒 等待锁线程
                        o.wait();   // 阻塞当前持有锁 线程
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                o.notifyAll(); // 必须，不然会有线程block住
            }
        },"t1").start();

        new Thread(()->{
            synchronized (o){
                for(char c:c2){
                    System.out.print(c);
                    try {
                        o.notify();
                        o.wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                o.notifyAll();
            }
        },"t2").start();
    }
}
