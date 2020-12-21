package com.xli.study.singleton;

/**
 * lazy-laod 并发问题
 */
public class SingleTon2 {

    private static SingleTon2 singleTon2;

    public static SingleTon2 getSingleTon2() {
        if (singleTon2 == null){
            singleTon2 = new SingleTon2();
        }
        return singleTon2;
    }

    public static void main(String[] args) {
        SingleTon2 singleTon2 = getSingleTon2();
    }
}
