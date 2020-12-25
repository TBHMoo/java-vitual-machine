package com.xli.study.olexec.execute;

import com.sun.org.apache.bcel.internal.util.ClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;

/**
 * @author: 李翔  lixiang1@baozun.com
 * @time: 2020-12-25 13:37
 */
public class JavaClassExecutor{

    /**
     * 类加载
     * 反射执行main 方法
     * @param classBytes
     * @param systemIn
     * @return
     */
    public static String execute(byte[] classBytes,String systemIn) {

        HotSwapClassLoader hotSwapClassLoader = new HotSwapClassLoader();
        Class clazz = hotSwapClassLoader.loadBytes(classBytes);
        try{

            Method mainMethod = clazz.getMethod("main", new Class[] { String[].class });
            mainMethod.invoke(null, new String[] { systemIn });
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch (InvocationTargetException e){
            e.printStackTrace();
        }
        return null;
    }
}
