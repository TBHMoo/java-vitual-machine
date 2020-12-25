package com.xli.study.olexec.execute;

/**
 * @author: 李翔  lixiang1@baozun.com
 * @time: 2020-12-25 14:02
 */
public class HotSwapClassLoader extends ClassLoader{


    public HotSwapClassLoader(){
        super(HotSwapClassLoader.class.getClassLoader());
    }

    public Class loadBytes(byte[] classBytes){
        return defineClass( null,classBytes,  0,  classBytes.length);
    }
}
