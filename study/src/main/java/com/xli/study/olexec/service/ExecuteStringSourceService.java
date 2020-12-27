package com.xli.study.olexec.service;

import com.xli.study.olexec.compiler.StringSourceCompiler;
import com.xli.study.olexec.execute.JavaClassExecutor;
import org.springframework.stereotype.Service;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class ExecuteStringSourceService {


    private final static int corePoolSize = 5;
    private final static int maximumPoolSize = 5;
    private final static long keepAliveTime = 0;
    private final static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(5);
    private final static ExecutorService pool = new ThreadPoolExecutor(corePoolSize,
                                                            maximumPoolSize,
                                                            keepAliveTime,
                                                            SECONDS,
                                                            workQueue);

    private final static long THREAD_MAX_WAIT_TIME = 15;
    /* 客户端发来的程序的运行时间限制 */
    private static final int RUN_TIME_LIMITED = 15*1000;


    /**
     * 编译-- 执行
     * @param source
     * @param systemIn
     * @return
     */
    public String execute(String source, String systemIn){
        DiagnosticCollector<JavaFileObject> compilerCollector = new DiagnosticCollector<>();//编译结果收集器
        StringBuilder runResult = new StringBuilder();

        // 编译
        final byte[] classBytes = StringSourceCompiler.compile(source,compilerCollector);
        // 编译出错
        if (null == classBytes){
            for (Diagnostic diagnostic:compilerCollector.getDiagnostics()){
                runResult.append(diagnostic.toString());
                runResult.append(System.lineSeparator());

            }
            return runResult.toString();
        }
        // 执行

        Callable<String> runTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return JavaClassExecutor.execute(classBytes, systemIn);
            }
        };
        Future<String> res = null;
        try {
            res = pool.submit(runTask);
        } catch (RejectedExecutionException e) {
            return "WAIT_WARNING";
        }

        // 获取运行结果，处理非客户端代码错误
        try {
            runResult.append(res.get(RUN_TIME_LIMITED, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            runResult.append( "Program interrupted.");
        } catch (ExecutionException e) {
            runResult.append(e.toString());
        } catch (TimeoutException e) {
            runResult.append( "Time Limit Exceeded.");
        } finally {
            res.cancel(true);
        }
        if(null == runResult) {
            return "wooo ho 出错了";
        }
        System.out.println(runResult);
        return runResult.toString();
    }

        public static void main(String[] args) {
//            String source = "public class Run {"
//                            + "public static void main(String[] args) {"
//                            + "        System.out.print(999);"
//                            + "}"
//                            + "}";
//            ExecuteStringSourceService stringSourceService = new ExecuteStringSourceService();
//            stringSourceService.execute(source,null);
//            Matcher matcher = CLASS_PATTERN.matcher(source);
//            String className;
//            if (matcher.find()) {
//                className = matcher.group(1);
//            } else {
//                throw new IllegalArgumentException("No valid class");
//            }
//            System.out.println(className);
//
//            URI uri = URI.create("String:///" + className + JavaFileObject.Kind.SOURCE.extension);
//            System.out.println(uri);
//            DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>();
//            byte[] bytes = compile(source,compileCollector);
//            System.out.println(bytes);
//            for (Diagnostic diagnostic: compileCollector.getDiagnostics()){
//                System.out.println(diagnostic.toString());
//
//            }

            ByteArrayOutputStream baoStream = new ByteArrayOutputStream(1024);
            PrintStream cacheStream = new PrintStream(baoStream);
            PrintStream oldStream = System.out;
            System.setOut(cacheStream);//不打印到控制台
            String outString = new String();
            System.out.println("System.out.println:test");
            String strMsg = baoStream.toString();
            System.out.println(strMsg);

        }
}
