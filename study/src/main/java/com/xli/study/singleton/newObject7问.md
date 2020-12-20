# 关于 Object o = new Object()

## 1.请解释对象的创建过程（半初始化）
1.class load 到内存中 byteCode

2.解释成虚拟机指令

```shell script
1 new             // 申请内存空间
2 dup     
3 invokespectial  // 执行空间初始化方法  o = new Object();
4 astore_1        // 将成员变量 关联到 内存地址上
5 return 
```

## 2、DCL单例（Double Check Lock） 到底需不需要volatile ?

#### 单例 singleTon是什么,内存中只能存在一个对象/ static
- 单例中为什么会有 double check lock 写法
#### volatile语义
- 线程间可见
- 禁止指令重排序
#### DCL 单例，需要volatile 的原因
 
## 3、对象在内存中的存储结构布局 （JOL 查看内存布局）
## 4、对象头包括什么？
## 5、对线怎么定位？
## 6、对象怎么分配内存空间？

## 7、一个Object占多少字节 ？ 
- 64位情况下，16字节  mark word 8字节，classloader 4字节，padding 4字节
- classloader  UseCompressedClassPointers 默认压缩类型指针 8 -> 4