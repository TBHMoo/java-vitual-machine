package com.xli.study.singleton;

import org.openjdk.jol.info.ClassLayout;

public class newObjectSynchronized {
    int m= 8;
    public static void main(String[] args) {
        Object o = new newObjectSynchronized();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        synchronized (o){
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }
}
