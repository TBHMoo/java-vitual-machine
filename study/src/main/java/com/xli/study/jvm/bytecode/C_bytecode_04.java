package com.xli.study.jvm.bytecode;

public class C_bytecode_04 {
    public static void main(String[] args) {
//        System.out.println("xxxx");
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
        }
//        int a= 0;
        System.gc();
//[GC (System.gc())  69532K->66560K(125952K), 0.0011165 secs]
//[GC (System.gc())  69532K->66480K(125952K), 0.0013624 secs]

//[Full GC (System.gc())  66560K->891K(125952K), 0.0048242 secs]
//[Full GC (System.gc())  66480K->66427K(125952K), 0.0051906 secs]
    }
}