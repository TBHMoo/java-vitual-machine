package com.xli.study.jvm.bytecode;

public class C_bytecode_05 {
    static abstract class Human {
    }

    static class Man extends Human {
    }

    static class Woman extends Human {
    }

    public void sayHello(Human guy) {
        System.out.println("Hello guy!");
    }

    public void sayHello(Man man) {
        System.out.println("Hello man!");
    }

    public void sayHello(Woman woman) {
        System.out.println("Hello woman!");
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        C_bytecode_05 sr = new C_bytecode_05();
        sr.sayHello(man);
        sr.sayHello(woman);
        /* 输出：
        Hello guy!
        Hello guy!
        因为是根据变量的静态类型，也就是左面的类型：Human 来判断调用哪个方法，
        所以调用的都是 public void sayHello(Human guy)
        */
    }
}
