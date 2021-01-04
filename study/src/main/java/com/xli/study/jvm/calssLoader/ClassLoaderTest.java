package com.xli.study.jvm.calssLoader;

import java.io.InputStream;

public class ClassLoaderTest {

    /**
     * 一个类在JVM 中的唯一标识符 是 类加载器 + 类名
     * 类加载： 类名找到.class 文件 -> byte[] -> 加载成Class对象
     *
     * 下面例子 自定义了一个类加载器 myLoader ，重写了loadClass 方法未跳过了双亲委派类加载机制
     * @param args
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".")+1)+".class";
                    System.out.println("loadClass  =" + fileName);
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null){
                        return super.loadClass(name);
                    }
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    return defineClass(name,b,0,b.length);
                }catch (Exception e){
                    throw new ClassNotFoundException(name);
                }
            }
        };

        Object object  = myLoader.loadClass("com.xli.study.jvm.calssLoader.ClassLoaderTest").newInstance();
        System.out.println(object.getClass());
        //
        System.out.println(" 重写loadClass()方法的 自定义加载器myLoader，加载的 com.xli.study.jvm.calssLoader.ClassLoaderTest对象，与JVM默认启动加载的 com.xli.study.jvm.calssLoader.ClassLoaderTest 不相等  " + (object instanceof com.xli.study.jvm.calssLoader.ClassLoaderTest));
    }
}
