package com.xli.study.jvm.calssLoader;

/**
 * @author: 李翔  lixiang1@baozun.com
 * @time: 2020-12-30 11:51
 */
public class ClassLoaderTree {

    public static void main(String[] args) {
        ClassLoader loader = ClassLoaderTree.class.getClassLoader();
        while (loader != null) {
            System.out.println(loader.toString());
            loader = loader.getParent();
        }
    }
}