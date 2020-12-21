package com.xli.study.singleton;

public class SingleTon1 {

    private static SingleTon1 singleTon1 = new SingleTon1();

    public static SingleTon1 getSingleTon1() {
        return singleTon1;
    }

    public static void main(String[] args) {
        SingleTon1 singleTon1 =  getSingleTon1();
    }
}
