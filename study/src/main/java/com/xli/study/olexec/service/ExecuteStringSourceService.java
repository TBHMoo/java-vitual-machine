package com.xli.study.olexec.service;

import org.springframework.stereotype.Service;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

@Service
public class ExecuteStringSourceService {

    public String execute(String source, String systemIn){
        DiagnosticCollector<JavaFileObject> compilerCollector = new DiagnosticCollector<>();//编译结果收集器

//        byte[] classBytes = StringSource
        String runResult=null;
        return runResult;
    }
}
