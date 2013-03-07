package com.naphaso.alice.scripting;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/6/13
 * Time: 6:47 PM
 */


public class ScriptService {
    private static final Logger logger = LoggerFactory.getLogger(ScriptService.class);

    private GroovyShell shell;

    @Autowired
    private ApplicationContext context;

    public ScriptService() {
        Binding binding = new Binding();
        binding.setVariable("_context", context);
        shell = new GroovyShell(binding);

        /*
        ClassLoader parent = getClass().getClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);
        try {
            Class groovyClass = loader.parseClass(new File("main.groovy"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public String eval(String code) {
        return shell.evaluate(code, "main.groovy", "ScriptBase").toString();
    }
}
