package com.xli.study.olexec.compiler;


import javax.tools.*;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringSourceCompiler {

    private static Map<String, JavaFileObject> fileObjectMap = new ConcurrentHashMap<>();

    /** 使用 Pattern 预编译功能 */
    private static Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s*");

    public static byte[] compile(String source, DiagnosticCollector<JavaFileObject> compileCollector){

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        /**1 */Writer out= null;  //编译器的一个额外的输出 Writer，为 null 的话就是 System.err；
        /**2 */StandardJavaFileManager  fileManager= compiler.getStandardFileManager(compileCollector,null,null);  //文件管理器；
        /**3 */DiagnosticListener<? super JavaFileObject> diagnosticListener= null;  //诊断信息收集器；
        /**4 */Iterable<String> options= null;  //编译器的配置；
        /**5 */Iterable<String> classes= null;  //需要被 annotation processing 处理的类的类名；
        /**6 */Iterable<? extends JavaFileObject> compilationUnits= new ArrayList<>();  //要被编译的单元们，就是一堆 JavaFileObject。
        ArrayList<JavaFileObject> sourceJavaFileObjectList = new ArrayList();

        Matcher matcher = CLASS_PATTERN.matcher(source);
        String className;
        if (matcher.find()) {
            className = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No valid class");
        }

        compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList("String:///Run.java"));
        // 把源码字符串构造成JavaFileObject，供编译使用
//        TmpJavaFileObject sourceJavaFileObject = new TmpJavaFileObject(className,source);
//        sourceJavaFileObjectList.add(sourceJavaFileObject);


//        我们将自己实现的 JavaFileObject 和 JavaFileManager,两个类都实现为了 StringSourceCompiler 的内部类，
//        StringSourceCompiler 中有一个 private static Map<String, JavaFileObject> fileObjectMap = new ConcurrentHashMap<>() 属性用来存放编译好的字节码对象。

        Boolean result = compiler.getTask(null,fileManager,compileCollector,null,null, compilationUnits).call();
//        if (result && )
        return new byte[1];
    }

    public static class TmpJavaFileObject extends SimpleJavaFileObject {

        private String source;

        /**
         * 构造用来存储源代码的JavaFileObject
         * 需要传入源码source，然后调用父类的构造方法创建kind = Kind.SOURCE的JavaFileObject对象
         */
        public TmpJavaFileObject(String name,String source){
            super(URI.create("String:///" + name + Kind.SOURCE.extension), Kind.SOURCE);
            this.source = source;
        }
    }

    public static void main(String[] args) {
        String source = "public class Run {111}";
        Matcher matcher = CLASS_PATTERN.matcher(source);
        String className;
        if (matcher.find()) {
            className = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No valid class");
        }
        System.out.println(className);

        URI uri = URI.create("String:///" + className + JavaFileObject.Kind.SOURCE.extension);
        System.out.println(uri);
        DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>();
        compile(source,compileCollector);
        System.out.println(compileCollector.toString());
    }
}
