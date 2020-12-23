package com.xli.study.olexec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RunCodeController {


    private static final String defaultSource = "12323";

    @RequestMapping(path = {"/"}, method = RequestMethod.GET)
    public String entry(Model model) {
        model.addAttribute("lastSource", defaultSource);
        return "ide";
    }

    @RequestMapping(path = {"/run"}, method = RequestMethod.POST)
    public String runCode(@RequestParam("source") String source,
                          @RequestParam("systemIn") String systemIn, Model model) {
//        String runResult = executeStringSourceService.execute(source, systemIn);
        String runResult = "hello";
//        runResult = runResult.replaceAll(System.lineSeparator(), "<br/>"); // 处理html中换行的问题

        model.addAttribute("lastSource", source);
        model.addAttribute("lastSystemIn", systemIn);
        model.addAttribute("runResult", runResult);
        return "ide";
    }
}
