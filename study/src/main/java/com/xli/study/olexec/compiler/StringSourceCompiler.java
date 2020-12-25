package com.xli.study.olexec.compiler;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringSourceCompiler {

    private static Map<String, JavaFileObject> fileObjectMap = new ConcurrentHashMap<>();

    /** 使用 Pattern 预编译功能 */
    private static Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s*");

    /**
     *     sourceString --> JavaFileObject --> getCharContent -->  MemJavaFileManager.getJavaFileForOutput ------> fileObjectMap.put()  ---> openOutputStream -->getCompiledBytes
     *     |-- 源码转成 javaFileObject 实现 getCharContent方法    |
     *                                                          | 实现getJavaFileForOutput用于接收，编译后的字节流数据-|                                                                                                          | 内存中准备用于接收编译后JavaFileObject,实现openOutputStream读取class字节流
     *
     * > 首先，要得到源码才能进行编译，所以会调用 JavaFileObject 的 getCharContent 方法，得到源码的字符序 CharSequence；
     * > 然后，编译器会对得到的源码进行编译，得到字节码，并且会将得到的字节码封装进一个 JavaFileObject 对象；
     * > 编译器会把字节码结果存入一个 JavaFileObject 中，这个操作是需要创建一个 JavaFileObject 对象的，可是我们用来真实存储源码和字节码的 JavaFileObject 对象是我们自己写的，那么编译器如何得知它应该把编译生成的字节码放入一个怎样的 JavaFileObject 中呢?
     * > 这时就要轮到 JavaFileManager 出场了，编译器会调用我们传入的 JavaFileManager fileManager 的 getJavaFileForOutput 方法，这个方法会 new 一个我们写的 TmpJavaFileObject 对象，并把返回给编译器；
     * > 接下来，编译器会把生成的字节码放在 TmpJavaFileObject 对象中，存放的位置是由我们自己指定的，在 TmpJavaFileObject 中加入一个 ByteArrayOutputStream 属性用于存储字节码，编译器会通过 openOutputStream() 来创建输出流对象，并把这个用来存储字节的容器返回给编译器，让它把编译生成的字节码放进去；
     * > 最后，我们想要的是 byte[] 字节数组，而非一个输出流，只要再在 TmpJavaFileObject 中加入一个 getCompiledBytes() 方法将 ByteArrayOutputStream 中的内容变成 byte[] 返回即可
     *
     * /
     * @param source
     * @param compileCollector
     * @return
     */
    public static byte[] compile(String source, DiagnosticCollector<JavaFileObject> compileCollector){

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //编译器的一个额外的输出 Writer，为 null 的话就是 System.err；
        Writer out= null;
        JavaFileManager javaFileManager = new MemJavaFileManager(compiler.getStandardFileManager(compileCollector,null,null));
        //诊断信息收集器；
        DiagnosticListener<? super JavaFileObject> diagnosticListener= null;
        //编译器的配置；
        Iterable<String> options= null;
        //需要被 annotation processing 处理的类的类名；
        Iterable<String> classes= null;
        //要被编译的单元们，就是一堆 JavaFileObject。
        Iterable<? extends JavaFileObject> compilationUnits= new ArrayList<>();
        ArrayList<JavaFileObject> sourceJavaFileObjectList = new ArrayList();

        Matcher matcher = CLASS_PATTERN.matcher(source);
        String className;
        if (matcher.find()) {
            className = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No valid class");
        }

        // 把源码字符串构造成JavaFileObject，供编译使用
        TmpJavaFileObject sourceJavaFileObject = new TmpJavaFileObject(className,source);
        sourceJavaFileObjectList.add(sourceJavaFileObject);

        Boolean result = compiler.getTask(null,javaFileManager,compileCollector,null,null, sourceJavaFileObjectList).call();
        JavaFileObject javaFileObject = fileObjectMap.get(className);
        if (result && javaFileObject!=null){
            return ((TmpJavaFileObject)javaFileObject).getCompiledBytes();
        }
        return null;
    }


    /**
     * getCharContent
     * getJavaFileForOutput
     * */
    public static class MemJavaFileManager extends ForwardingJavaFileManager<JavaFileManager>{


        /**
         * Creates a new instance of ForwardingJavaFileManager.
         *
         * @param fileManager delegate to this file manager
         */
        protected MemJavaFileManager(JavaFileManager fileManager){
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location,String className,JavaFileObject.Kind kind) throws IOException{
            JavaFileObject inputJavaFileObjet = fileObjectMap.get(className);
            if (null == inputJavaFileObjet){
                return super.getJavaFileForInput(location,className,kind);
            }
            return inputJavaFileObjet;
        }

        /**
         * 构造用来存储字节码的JavaFileObject
         * 需要传入kind，即我们想要构建一个存储什么类型文件的JavaFileObject
         */
        @Override
        public JavaFileObject getJavaFileForOutput(Location location,String className,JavaFileObject.Kind kind,FileObject sibling) throws IOException{
            JavaFileObject classJavaFileObject = new TmpJavaFileObject(className,kind);
            fileObjectMap.put(className,classJavaFileObject);
            return classJavaFileObject;
        }
    }

    public static class TmpJavaFileObject extends SimpleJavaFileObject {

        private String source;

        private ByteArrayOutputStream outputStream;
        /**
         * 构造用来存储源代码的JavaFileObject
         * 需要传入源码source，然后调用父类的构造方法创建kind = Kind.SOURCE的JavaFileObject对象
         */
        public TmpJavaFileObject(String name,String source){
            super(URI.create("String:///" + name + Kind.SOURCE.extension), Kind.SOURCE);
            this.source = source;
        }

        /**
         * 构造用来存储字节码的JavaFileObject
         * 需要传入kind，即我们想要构建一个存储什么类型文件的JavaFileObject
         */
        public TmpJavaFileObject(String name, Kind kind) {
            super(URI.create("String:///" + name + Kind.SOURCE.extension), kind);
            this.source = null;
        }


        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException{
            if(null == source){
                throw new IllegalArgumentException("source == null");
            }
            return source;
        }

        @Override
        public OutputStream openOutputStream() throws IOException {
            outputStream = new ByteArrayOutputStream();
            return outputStream;
        }

        public byte[] getCompiledBytes(){
            return outputStream.toByteArray();
        }
    }

//    public static void main(String[] args) {
//        String source = "public class Run {"
//                        + "public static void main(String[] args) {"
//                        + "        System.out.print(999);"
//                        + "}"
//                        + "}";
//        Matcher matcher = CLASS_PATTERN.matcher(source);
//        String className;
//        if (matcher.find()) {
//            className = matcher.group(1);
//        } else {
//            throw new IllegalArgumentException("No valid class");
//        }
//        System.out.println(className);
//
//        URI uri = URI.create("String:///" + className + JavaFileObject.Kind.SOURCE.extension);
//        System.out.println(uri);
//        DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>();
//        byte[] bytes = compile(source,compileCollector);
//        System.out.println(bytes);
//        for (Diagnostic diagnostic: compileCollector.getDiagnostics()){
//            System.out.println(diagnostic.toString());
//
//        }
//    }
}
