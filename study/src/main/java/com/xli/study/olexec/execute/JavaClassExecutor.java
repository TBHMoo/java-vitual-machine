package com.xli.study.olexec.execute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

        ClassModifier classModifier = new ClassModifier(classBytes);
        // 为了避免 客户端程序，与服务器争抢 标准输入输出资源，
        //         和客户端程序运行关闭虚拟机命令
        // 替換System 为 HackSystem
        byte[] modifyBytes =  classModifier.modifyConstantUtf8Info("java/lang/System","com/xli/study/olexec/execute/HackSystem");

        HotSwapClassLoader hotSwapClassLoader = new HotSwapClassLoader();
        Class clazz = hotSwapClassLoader.loadBytes(modifyBytes);

//
        StringBuilder stringBuilder = new StringBuilder();
//        ByteArrayOutputStream baoStream = new ByteArrayOutputStream(1024);
//        PrintStream cacheStream = new PrintStream(baoStream);
//        PrintStream oldStream = System.out;
//        System.setOut(cacheStream);//不打印到控制台

        try{

            Method mainMethod = clazz.getMethod("main", new Class[] { String[].class });
            mainMethod.invoke(null, new String[] { null });
        }catch (NoSuchMethodException e){
            e.printStackTrace();
            stringBuilder.append(e.getCause().getMessage());
        }catch (IllegalAccessException e){
            e.printStackTrace();
            stringBuilder.append(e.getCause().getMessage());
        }catch (InvocationTargetException e){
            e.printStackTrace();
            stringBuilder.append(e.getCause().getMessage());
        }

        // 6. 从HackSystem中获取返回结果getBufferString
        String res = HackSystem.getBufferString();
        HackSystem.closeBuffer();
//
//        String outString = new String();
//        System.out.println("System.out.println:test");
//        String strMsg = baoStream.toString();

        stringBuilder.append(res);
        return stringBuilder.toString().replace("\n","<br/>");
    }
}
