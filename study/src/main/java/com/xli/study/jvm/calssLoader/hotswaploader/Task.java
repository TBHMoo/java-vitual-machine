package com.xli.study.jvm.calssLoader.hotswaploader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: 李翔  lixiang1@baozun.com
 * @time: 2020-12-30 17:11
 */
public class Task extends TimerTask{

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run(){
        String basePath = "E:\\project\\java-vitual-machine\\study\\target\\classes";
        // 每个线程都热替换
        HotSwapClassLoader hotSwapClassLoader = new HotSwapClassLoader(basePath,new String[]{"com.xli.study.jvm.calssLoader.hotswaploader.Foo"});
        try{
            Class clz = hotSwapClassLoader.loadClass("com.xli.study.jvm.calssLoader.hotswaploader.Foo",true);
            Object foo = clz.newInstance();
            Method method = clz.getMethod("sayHello",new Class[]{});
            method.invoke(foo,new Class[]{});
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch (InvocationTargetException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Timer timer= new Timer();
        timer.schedule(new Task(),0,10000);
    }

}
