# 关于 Object o = new Object()

## 1.请解释对象的创建过程（半初始化）
```java
public class newObject {
    int m= 8;
    public static void main(String[] args) {
        Object o = new newObject();
    }
}
```
汇编码
```
0 new #2 <java/lang/Object>  // 在内存中申请空间  m= 0,半初始化状态 类比 c语言 malloc+初始化
3 dup  //Duplicate the top operand stack value
4 invokespecial #1 <java/lang/Object.<init>>  // 调用构造方法， m= 8 ，指令重排
7 astore_1  // 将变量o 和内存空间关联 //  指令重排
8 return
```

## 2、DCL单例（Double Check Lock） 到底需不需要volatile ?

#### 单例 singleTon是什么,内存中只能存在一个对象/ static
- 单例中为什么会有 double check lock 写法
#### volatile语义
- 线程间可见
- 禁止指令重排序
- volatile 修饰变量保证原子性，虚拟机规范要求 总线锁->MESI—> CPU缓存一致性
#### DCL 单例，需要volatile 的原因 
o = new Object() 不是原子性的， 汇编指令只有8个是原子性的 

 
## 3、对象在内存中的存储结构布局 （JOL 查看内存布局）
## 4、对象头包括什么？
## 5、对线怎么定位？
## 6、对象怎么分配内存空间？


## 7、一个Object占多少字节 ？ 
- 64位情况下，16字节  mark word 8字节，classloader 4字节，padding 4字节
- classloader  UseCompressedClassPointers 默认压缩类型指针 8 -> 4