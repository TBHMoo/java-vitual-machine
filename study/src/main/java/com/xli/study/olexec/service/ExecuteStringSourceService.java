package com.xli.study.olexec.service;

import com.xli.study.olexec.compiler.StringSourceCompiler;
import com.xli.study.olexec.execute.JavaClassExecutor;
import org.springframework.stereotype.Service;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.net.URI;

@Service
public class ExecuteStringSourceService {

    /**
     * 编译-- 执行
     * @param source
     * @param systemIn
     * @return
     */
    public String execute(String source, String systemIn){
        DiagnosticCollector<JavaFileObject> compilerCollector = new DiagnosticCollector<>();//编译结果收集器

        // 编译
        final byte[] classBytes = StringSourceCompiler.compile(source,compilerCollector);
        // 编译出错
        if (null == classBytes){
//            compilerCollector;
        }
        // 执行
        JavaClassExecutor.execute(classBytes,systemIn);

        String runResult=null;
        return runResult;
    }

        public static void main(String[] args) {
            String source = "public class Run {"
                            + "public static void main(String[] args) {"
                            + "        System.out.print(999);"
                            + "}"
                            + "}";
            ExecuteStringSourceService stringSourceService = new ExecuteStringSourceService();
            stringSourceService.execute(source,"");
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
        }
}
