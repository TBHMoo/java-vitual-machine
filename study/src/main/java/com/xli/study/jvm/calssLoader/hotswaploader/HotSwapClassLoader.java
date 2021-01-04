package com.xli.study.jvm.calssLoader.hotswaploader;

import java.io.*;
import java.util.HashSet;

/**
 * @author: 李翔  lixiang1@baozun.com
 * @time: 2020-12-30 17:16
 */
public class HotSwapClassLoader extends ClassLoader{


    private String basePath ;
    private HashSet<String> loadedClass;


    public HotSwapClassLoader(String basePath,String[] classNameList){
        super(null);
        this.basePath = basePath;

        loadedClass = new HashSet<>();
        loadClassByMe(classNameList);
    }

    private void loadClassByMe(String[] classNameList){
        // name to loadedClass
        for (int i=0;i<classNameList.length;i++){
            Class cls = loadClassByClassName(classNameList[i]);
            if(null != cls){
                loadedClass.add(classNameList[i]);
            }
        }
    }

    /**
     * className ----> file ---> InputStream ---> byte[] --->defineClass() ----> return Class
     * @param className
     * @return
     */
    private Class loadClassByClassName(String className){
        StringBuilder fileName = new StringBuilder(basePath);
        String classPath = className.replace(".",File.separator) + ".class";
        fileName.append(File.separator + classPath);

        File file = new File(fileName.toString());
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            inputStream.read(bytes);
            return defineClass(className,bytes,0,(int)file.length());
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Class loadClass(String name,boolean resolve) throws ClassNotFoundException{
        Class clazz = null;
        clazz = findLoadedClass(name);
        if (clazz == null && !loadedClass.contains(name)){
            clazz= findSystemClass(name);
        }

        if (clazz == null){
            throw new ClassNotFoundException();
        }

        if (resolve){
            resolveClass(clazz);
        }
        return clazz;
    }

}
