package com.xli.study.singleton;

/**
 * lazy-laod 并发问题,加锁，效率问题
 */
public class SingleTon3 {

    private static SingleTon3 singleTon3;

    public static SingleTon3 getSingleTon3() {
        if (singleTon3 == null){ // Double check lock
            synchronized (singleTon3){
                if (singleTon3 == null){
                    singleTon3 = new SingleTon3();
                }
            }
        }
        return singleTon3;
    }

    public static void main(String[] args) {
        SingleTon3 singleTon2 = getSingleTon3();
    }
}
