package com.xli.study.singleton;

import org.openjdk.jol.info.ClassLayout;

public class newObject {
    int m= 8;
    public static void main(String[] args) {
        Object o = new newObject();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}
